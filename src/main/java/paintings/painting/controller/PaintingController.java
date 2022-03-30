package paintings.painting.controller;

import paintings.artist.dto.GetArtistsResponse;
import paintings.artist.service.ArtistService;
import paintings.painting.controller.PaintingController;
import paintings.painting.dto.CreatePaintingRequest;
import paintings.painting.dto.GetPaintingsResponse;
import paintings.painting.dto.UpdatePaintingRequest;
import paintings.painting.entity.Painting;
import paintings.painting.dto.CreatePaintingRequest;
import paintings.painting.entity.Painting;
import paintings.painting.service.PaintingService;
import paintings.servlet.MimeTypes;
import paintings.user.entity.UserRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.util.Optional;

@Path("/paintings")
public class PaintingController {

    private PaintingService service;
    //private ArtistService artistService;

    public PaintingController() {
    }

    @EJB
    public void setService(PaintingService service) {
        this.service = service;
    }

/*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePainting(CreatePaintingRequest request) {
        Painting painting = CreatePaintingRequest
                .dtoToEntityMapper(artist -> artistService.find(artist).orElse(null))
                .apply(request);
        service.create(painting);
        return Response.created(UriBuilder.fromMethod(ArtistPaintingController.class, "getPainting")
                .build(painting.getId())).build();
    }
*/
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response putPainting(@PathParam("id") Long id, UpdatePaintingRequest request) {
        Optional<Painting> painting = service.find(id);

        if (painting.isPresent()) {
            UpdatePaintingRequest.dtoToEntityUpdater().apply(painting.get(), request);

            service.update(painting.get());
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response deletePainting(@PathParam("id") Long id) {
        service.delete(id);
        return Response.ok().build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response getPaintings() {
        return Response.ok(GetPaintingsResponse.entityToDtoMapper().apply(service.findAll())).build();
    }
}
