package paintings.painting.repository;

import paintings.artist.entity.Artist;
import paintings.user.entity.User;
import paintings.painting.entity.Painting;
import paintings.repository.Repository;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Repository for painting entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class PaintingRepository implements Repository<Painting, Long> {

    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Painting> find(Long id) {
        return Optional.ofNullable(em.find(Painting.class, id));
    }
    @Override
    public List<Painting> findAll() {
        return em.createQuery("select p from Painting p", Painting.class).getResultList();
    }
    @Override
    public void create(Painting entity) {
        em.persist(entity);
    }
    @Override
    public void delete(Painting entity) {
        em.remove(em.find(Painting.class, entity.getId()));
    }
    @Override
    public void update(Painting entity) {
        em.merge(entity);
    }
    @Override
    public void detach(Painting entity) {
        em.detach(entity);
    }

    public List<Painting> findAllByArtist(Artist artist) {
        System.out.println("Kams art:" + artist);
        return em.createQuery("select p from Painting p where p.artist = :artist", Painting.class)
                .setParameter("artist", artist)
                .getResultList();
    }

    public List<Painting> findAllByUser(User user) {
        System.out.println("Kams user:" + user);
        System.out.println(em.createQuery("select p from Painting p", Painting.class)
                .getResultList());
        return em.createQuery("select p from Painting p where p.user = :user", Painting.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Optional<Painting> findByUserAndId(User user, Long id){
        try {
            return Optional.of(em.createQuery("select p from Painting p where p.id = :id and p.user = :user", Painting.class)
                    .setParameter("user", user)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Painting> findByArtistAndId(Artist artist, Long id){
        try {
            return Optional.of(em.createQuery("select p from Painting p where p.id = :id and p.artist = :artist", Painting.class)
                    .setParameter("artist", artist)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }


}
