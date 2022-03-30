package paintings.user.service;

import lombok.NoArgsConstructor;
import paintings.user.entity.User;
import paintings.user.entity.UserRoles;
import paintings.user.repository.UserRepository;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity. As this is an EJB bean, transactions (if not
 * configured otherwise) are started automatically for each method called by proxy.
 */
@Stateless
@LocalBean
@NoArgsConstructor
@RolesAllowed(UserRoles.USER)
public class UserService {

    /**
     * Mock of the database. Should be replaced with repository layer.
     */
    private UserRepository repository;

    /**
     * Build in security context.
     */
    private SecurityContext securityContext;

    /**
     * Password hashing algorithm.
     */
    private Pbkdf2PasswordHash pbkdf;

    @Inject
    public UserService(UserRepository repository, SecurityContext securityContext, Pbkdf2PasswordHash pbkdf) {
        this.repository = repository;
        this.securityContext = securityContext;
        this.pbkdf = pbkdf;
    }

    /**
     * @param login existing username
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.find(login);
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * Stores new user in the storage. Everyone can call it. If caller principal is not admin roles narrowed to
     * {@link UserRoles#USER}.
     *
     * @param user new user
     */
    @PermitAll
    public void create(User user) {
        if (!securityContext.isCallerInRole(UserRoles.ADMIN)) {
            user.setRoles(List.of(UserRoles.USER));
        }
        user.setPassword(pbkdf.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    /**
     * @return all available users
     */
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * @return logged user entity
     */
    public Optional<User> findCallerPrincipal() {
        if (securityContext.getCallerPrincipal() != null) {
            return find(securityContext.getCallerPrincipal().getName());
        } else {
            return Optional.empty();
        }
    }

    /**
     * @param user user to be removed
     */
    @RolesAllowed(UserRoles.ADMIN)
    public void delete(User user) {
        repository.delete(user);
    }
}
