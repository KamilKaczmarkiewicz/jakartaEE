package paintings.painting.controller;

import paintings.painting.dto.CreatePaintingRequest;
import paintings.painting.dto.GetPaintingResponse;
import paintings.painting.dto.GetPaintingsResponse;
import paintings.painting.dto.UpdatePaintingRequest;
import paintings.painting.entity.Painting;
import paintings.painting.service.PaintingService;
import paintings.artist.service.ArtistService;
import paintings.controller.interceptor.binding.CatchEjbException;
import paintings.servlet.MimeTypes;
import paintings.user.entity.UserRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for {@link Painting} entity. The controller uses hierarchical mapping relative to users.
 */
@Path("")
@RolesAllowed(UserRoles.USER)
public class UserPaintingController {

    /**
     * Service for managing paintings.
     */
    private PaintingService paintingService;

    /**
     * Service for managing artists.
     */
    private ArtistService artistService;

    /**
     * JAX-RS requires no-args constructor.
     */
    public UserPaintingController() {
    }

    /**
     * @param paintingService service for managing paintings
     */
    @EJB
    public void setPaintingService(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    /**
     * @param artistService service for managing artists
     */
    @EJB
    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * @param login user's username
     * @return response with available paintings owned by selected user
     */
    @GET
    @Path("/users/{login}/paintings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaintings(@PathParam("login") String login) {
        Optional<List<Painting>> paintings = paintingService.findAllByUser(login);
        if (paintings.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } else {
            System.out.println(1);
            return Response
                    .ok(GetPaintingsResponse.entityToDtoMapper().apply(paintings.get()))
                    .build();
        }
    }

    /**
     * @param login user's username
     * @param id    id of the painting
     * @return response with selected painting or 404 status code
     */
    @GET
    @Path("/users/{login}/paintings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPainting(@PathParam("login") String login, @PathParam("id") Long id) {
        System.out.println(69);
        Optional<Painting> painting = paintingService.findByUser(login, id);
        System.out.println(painting);
        if (painting.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } else {
            return Response
                    .ok(GetPaintingResponse.entityToDtoMapper().apply(painting.get()))
                    .build();
        }
    }


    /**
     * @return response with available paintings owned by logged user
     */
    @GET
    @Path("/user/paintings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPaintings() {
        return Response
                .ok(GetPaintingsResponse.entityToDtoMapper().apply(paintingService.findAllForCallerPrincipal()))
                .build();

    }

    /**
     * @param id id of the painting
     * @return response with selected painting or 404 status code
     */
    @GET
    @Path("/user/paintings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPainting(@PathParam("id") Long id) {
        Optional<Painting> painting = paintingService.findForCallerPrincipal(id);
        if (painting.isPresent()) {
            return Response
                    .ok(GetPaintingResponse.entityToDtoMapper().apply(painting.get()))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }


    /**
     * Creates new painting.
     *
     * @param request parsed request body containing info about new painting
     * @return response with created code and new painting location url
     */
    @POST
    @Path("/user/paintings")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUserPainting(@Valid CreatePaintingRequest request) {
        Painting painting = CreatePaintingRequest
                .dtoToEntityMapper(name -> artistService.find(name).orElse(null), () -> null)
                .apply(request);
        paintingService.createForCallerPrincipal(painting);
        return Response
                .created(UriBuilder
                        .fromMethod(UserPaintingController.class, "getUserPainting")
                        .build(painting.getId()))
                .build();
    }

    /**
     * Updates painting.
     *
     * @param request parsed request body containing info about painting
     * @return response with accepted code
     */
    @PUT
    @Path("/user/paintings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUserPainting(@PathParam("id") Long id, UpdatePaintingRequest request) {
        Optional<Painting> painting = paintingService.findForCallerPrincipal(id);

        if (painting.isPresent()) {
            UpdatePaintingRequest.dtoToEntityUpdater().apply(painting.get(), request);

            paintingService.update(painting.get());
            return Response
                    .noContent()
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    /**
     * Updates painting.
     *
     * @param request parsed request body containing info about painting
     * @return response with accepted code
     */
    @CatchEjbException
    @PUT
    @Path("/users/{login}/paintings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPainting(@PathParam("login") String login, @PathParam("id") Long id, UpdatePaintingRequest request) {
        Optional<Painting> painting = paintingService.findByUser(login, id);

        if (painting.isPresent()) {
            UpdatePaintingRequest.dtoToEntityUpdater().apply(painting.get(), request);

            paintingService.update(painting.get());
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
