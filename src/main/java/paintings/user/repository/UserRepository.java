package paintings.user.repository;

import lombok.extern.java.Log;
import paintings.repository.Repository;
import paintings.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity. Repositories should be used in business layer (e.g.: in services). There is no need
 *  for defining request scoped as repositories will be injected only to EJB beans which by design are not shared across
 *  different threads.
 */
@Dependent
@Log
public class UserRepository implements Repository<User, String> {

    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> find(String id) {
        log.info(String.format("EntityManager for %s %s find", this.getClass(), em));
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void create(User entity) {
        log.info(String.format("EntityManager for %s %s create", this.getClass(), em));
        em.persist(entity);
    }

    @Override
    public void delete(User entity) {
        em.remove(em.find(User.class, entity.getLogin()));
    }

    @Override
    public void update(User entity) {
        em.merge(entity);
    }

    @Override
    public void detach(User entity) {
        em.detach(entity);
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return Optional.of(em.createQuery("select u from User u where u.login = :login and u.password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

}
