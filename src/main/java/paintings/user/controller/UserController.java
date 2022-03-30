package paintings.user.controller;

import paintings.user.dto.CreateUserRequest;
import paintings.user.dto.GetUserResponse;
import paintings.user.dto.GetUsersResponse;
import paintings.user.entity.User;
import paintings.user.entity.UserRoles;
import paintings.user.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

/**
 * REST controller for {@link paintings.user.entity.User} entity.
 */
@Path("")
@RolesAllowed(UserRoles.USER)
public class UserController {

    /**
     * Service for managing users.
     */
    private UserService service;

    /**
     * JAX-RS requires no-args constructor.
     */
    public UserController() {
    }

    /**
     * @param service service for managing users
     */
    @EJB
    public void setService(UserService service) {
        this.service = service;
    }

    /**
     * @return response with available users
     */
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        return Response
                .ok(GetUsersResponse.entityToDtoMapper().apply(service.findAll()))
                .build();
    }

    /**
     * @param login user's username
     * @return response with selected user
     */
    @GET
    @Path("/users/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("login") String login) {
        Optional<User> user = service.find(login);
        if (user.isPresent()) {
            return Response
                    .ok(GetUserResponse.entityToDtoMapper().apply(user.get()))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    /**
     * @return response with logged user or empty object
     */
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        Optional<User> user = service.findCallerPrincipal();
        if (user.isPresent()) {
            return Response
                    .ok(GetUserResponse.entityToDtoMapper().apply(user.get()))
                    .build();
        } else {
            return Response
                    .ok(new Object())
                    .build();
        }
    }

    /**
     * Removes selected user.
     *
     * @return response with accepted or not found
     */
    @Path("/users/{login}")
    @DELETE
    @RolesAllowed(UserRoles.ADMIN)
    public Response deleteUser(@PathParam("login") String login) {
        Optional<User> user = service.find(login);
        if (user.isPresent()) {
            service.delete(user.get());
            return Response
                    .noContent()
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @Path("/users")
    @POST
    @PermitAll
    public Response createUser(CreateUserRequest request) {
        service.create(CreateUserRequest.dtoToEntityMapper().apply(request));
        return Response.created(UriBuilder.fromPath("/users/{login}")
                .build(request.getLogin())).build();
    }

}
