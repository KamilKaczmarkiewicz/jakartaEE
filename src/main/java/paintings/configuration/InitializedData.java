package paintings.configuration;

import lombok.SneakyThrows;
import paintings.artist.entity.Artist;
import paintings.artist.service.ArtistService;
import paintings.digest.Sha256Utility;
import paintings.painting.entity.Painting;
import paintings.painting.service.PaintingService;
import paintings.user.entity.User;
import paintings.user.entity.UserRoles;
import paintings.user.service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * Listener started automatically on CDI application context initialized. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
public class InitializedData {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    @Inject
    public InitializedData(Pbkdf2PasswordHash pbkdf) {
        this.pbkdf = pbkdf;
    }

    public InitializedData() {
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should
     * be created only once.
     */
    @PostConstruct
    private synchronized void init() {
        User kamil = User.builder()
                .login("kamil")
                .name("Kamil")
                .surname("Kowal")
                .birthDate(LocalDate.of(1990, 10, 21))
                .email("kamil@gmail.com")
                .password(pbkdf.generate("useruser".toCharArray()))
                .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                .build();

        User adam = User.builder()
                .login("adam")
                .name("Adam")
                .surname("Kaczmarek")
                .birthDate(LocalDate.of(2001, 1, 16))
                .email("adam@gmail.com")
                .password(pbkdf.generate("useruser".toCharArray()))
                .roles(List.of(UserRoles.USER))
                .build();

        User kasia = User.builder()
                .login("kasia")
                .name("Kasia")
                .surname("Drzewko")
                .birthDate(LocalDate.of(2002, 3, 19))
                .email("kasia@gmail.com")
                .password(pbkdf.generate("useruser".toCharArray()))
                .roles(List.of(UserRoles.USER))
                .build();

        User ola = User.builder()
                .login("ola")
                .name("Ola")
                .surname("Mokra")
                .birthDate(LocalDate.of(2002, 3, 19))
                .email("ola@gmail.com")
                .password(pbkdf.generate("useruser".toCharArray()))
                .roles(List.of(UserRoles.USER))
                .build();

        em.persist(kamil);
        em.persist(adam);
        em.persist(kasia);
        em.persist(ola);

        Artist leo = Artist.builder()
                .name("Leo")
                .birthDate(LocalDate.of(1993, 3, 13))
                .isAlive(true)
                .build();
        Artist michael = Artist.builder()
                .name("Michael")
                .birthDate(LocalDate.of(1982, 2, 12))
                .isAlive(true)
                .build();
        Artist van = Artist.builder()
                .name("Van")
                .birthDate(LocalDate.of(1971, 1, 11))
                .isAlive(false)
                .build();

        em.persist(leo);
        em.persist(michael);
        em.persist(van);

        Painting img1 = Painting.builder()
                .name("Zrobienie Marka")
                .creationDate(LocalDate.of(2000, 1, 11))
                .description("opis numero jeden")
                .likes(1)
                .artist(leo)
                .user(kamil)
                .build();
        Painting img2 = Painting.builder()
                .name("Mandarynki")
                .creationDate(LocalDate.of(2001, 1, 11))
                .description("opis numero dwa")
                .likes(12)
                .artist(leo)
                .user(kamil)
                .build();
        Painting img3 = Painting.builder()
                .name("Oryginalna nazwa")
                .creationDate(LocalDate.of(2002, 2, 12))
                .description("opis numero trzy")
                .likes(13)
                .artist(leo)
                .user(kamil)
                .build();
        Painting img4 = Painting.builder()
                .name("Niepokonnany")
                .creationDate(LocalDate.of(2003, 3, 13))
                .description("opis numero cztery")
                .likes(44)
                .artist(michael)
                .user(adam)
                .build();
        Painting img5 = Painting.builder()
                .name("Zrobienie Adama")
                .creationDate(LocalDate.of(2004, 4, 14))
                .description("opis numero pieć")
                .likes(5)
                .artist(michael)
                .user(adam)
                .build();
        Painting img6 = Painting.builder()
                .name("Chusteczki")
                .creationDate(LocalDate.of(2005, 5, 15))
                .description("opis numero sześć")
                .likes(66)
                .artist(van)
                .user(adam)
                .build();

        em.persist(img1);
        em.persist(img2);
        em.persist(img3);
        em.persist(img4);
        em.persist(img5);
        em.persist(img6);


        leo.setPaintings(List.of(img1, img2, img3));
        michael.setPaintings(List.of(img4, img5));
        van.setPaintings(List.of(img6));
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            return is.readAllBytes();
        }
    }

}
