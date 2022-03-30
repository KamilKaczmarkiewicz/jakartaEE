package paintings.artist.service;

import lombok.NoArgsConstructor;
import paintings.artist.entity.Artist;
import paintings.artist.repository.ArtistRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding character's artist entity.
 */
@Stateless
@LocalBean
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class ArtistService {

    private ArtistRepository repository;

    @Inject
    public ArtistService(ArtistRepository repository) {
        this.repository = repository;
    }

    public Optional<Artist> find(String name) {
        return repository.find(name);
    }

    public void create(Artist artist) {
        repository.create(artist);
    }

    public List<Artist> findAll() {
        return repository.findAll();
    }

    public void update(Artist artist) {
        repository.update(artist);
    }

    public void delete(Artist artist) {
        repository.delete(artist);
    }
}
