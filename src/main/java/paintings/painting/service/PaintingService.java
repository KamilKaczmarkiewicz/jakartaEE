package paintings.painting.service;

import lombok.NoArgsConstructor;
import paintings.artist.entity.Artist;
import paintings.artist.repository.ArtistRepository;
import paintings.painting.entity.Painting;
import paintings.painting.repository.PaintingRepository;
import paintings.user.authentication.interceptor.binding.AllowAdminOrOwner;
import paintings.user.entity.User;
import paintings.user.entity.UserRoles;
import paintings.user.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.transaction.Transactional;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding painting entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
@RolesAllowed(UserRoles.USER)
public class PaintingService {

    private PaintingRepository paintingRepository;
    private ArtistRepository artistRepository;
    private UserRepository userRepository;
    private SecurityContext securityContext;


    @Inject
    public PaintingService(PaintingRepository paintingRepository,
                           ArtistRepository artistRepository,
                           UserRepository userRepository,
                           SecurityContext securityContext) {
        this.paintingRepository = paintingRepository;
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }


    public Optional<Painting> find(Long id) {
        return paintingRepository.find(id);
    }
    public Optional<Painting> findByArtist(String name, Long id) {
        Optional<Artist> artist = artistRepository.find(name);
        if (artist.isEmpty()) {
            return Optional.empty();
        } else {
            return paintingRepository.findByArtistAndId(artist.get(), id);
        }
    }
    public Optional<Painting> findForCallerPrincipal(Long id) {
        return paintingRepository.findByUserAndId(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow(), id);
    }
    public Optional<Painting> findByUser(String login, Long id) {
        Optional<User> user = userRepository.find(login);
        if (user.isEmpty()) {
            System.out.println(-123);
            return Optional.empty();
        } else {
            System.out.println(123);
            return paintingRepository.findByUserAndId(user.get(), id);
        }
    }




    public List<Painting> findAll() {
        return paintingRepository.findAll();
    }
    public Optional<List<Painting>> findAllByArtist(String name) {
        Optional<Artist> artist = artistRepository.find(name);
        if (artist.isPresent()) {
            return Optional.of(paintingRepository.findAllByArtist(artist.get()));
        } else {
            return Optional.empty();
        }
    }
    public List<Painting> findAllForCallerPrincipal() {
        System.out.println(securityContext.getCallerPrincipal().getName());
        System.out.println(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
        return paintingRepository.findAllByUser(userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow());
    }
    public Optional<List<Painting>> findAllByUser(String login) {
        Optional<User> user = userRepository.find(login);
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(paintingRepository.findAllByUser(user.get()));
        }
    }




    public List<Painting> findAllForArtist(Artist artist) {
        return paintingRepository.findAllByArtist(artist);
    }

    public void create(Painting painting) {
        paintingRepository.create(painting);
    }

    public void createForCallerPrincipal(Painting painting) {
        User user = userRepository.find(securityContext.getCallerPrincipal().getName()).orElseThrow();
        painting.setUser(user);
        user.getPaintings().add(painting);
        paintingRepository.create(painting);
    }

    @AllowAdminOrOwner
    public void update(Painting painting) throws EJBAccessException {
        Painting original = paintingRepository.find(painting.getId()).orElseThrow();
        paintingRepository.detach(original);
        if (!original.getUser().getLogin().equals(painting.getUser().getLogin())) {
            original.getUser().getPaintings().removeIf(paintingToRemove -> paintingToRemove.getId().equals(painting.getId()));
            userRepository.find(painting.getUser().getLogin()).ifPresent(user -> user.getPaintings().add(painting));
        }
        paintingRepository.update(painting);
    }

    @AllowAdminOrOwner
    public void delete(Long paintingId) throws EJBAccessException {
        Painting painting = paintingRepository.find(paintingId).orElseThrow();
        painting.getUser().getPaintings().remove(painting);
        paintingRepository.delete(painting);
    }

}
