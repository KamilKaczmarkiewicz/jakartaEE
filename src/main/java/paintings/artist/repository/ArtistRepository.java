package paintings.artist.repository;

import paintings.artist.entity.Artist;
import paintings.repository.Repository;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Repository for profession entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class ArtistRepository implements Repository<Artist, String> {

    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }


    @Override
    public Optional<Artist> find(String id) {
        return Optional.ofNullable(em.find(Artist.class, id));
    }

    @Override
    public List<Artist> findAll() {
        return em.createQuery("select a from Artist a", Artist.class).getResultList();
    }

    @Override
    public void create(Artist entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Artist entity) {
        em.remove(em.find(Artist.class, entity.getName()));
    }

    @Override
    public void update(Artist entity) {
        em.merge(entity);
    }

    @Override
    public void detach(Artist entity) {
        em.detach(entity);
    }


}
