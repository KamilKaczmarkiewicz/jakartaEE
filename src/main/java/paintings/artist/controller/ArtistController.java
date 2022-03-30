package paintings.artist.controller;

import paintings.artist.dto.CreateArtistRequest;
import paintings.artist.dto.GetArtistResponse;
import paintings.artist.dto.GetArtistsResponse;
import paintings.artist.dto.UpdateArtistRequest;
import paintings.artist.entity.Artist;
import paintings.artist.service.ArtistService;
import paintings.user.entity.UserRoles;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Path("/artists")
@RolesAllowed(UserRoles.USER)
public class ArtistController {

    private ArtistService service;

    public ArtistController() {
    }

    @EJB
    public void setService(ArtistService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getArtists() {
        return Response.ok(GetArtistsResponse.entityToDtoMapper().apply(service.findAll())).build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtist(@PathParam("name") String name) {
        Optional<Artist> artist = service.find(name);
        if (artist.isPresent()) {
            return Response.ok(GetArtistResponse.entityToDtoMapper().apply(artist.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ADMIN)
    public Response saveArtist(CreateArtistRequest request) {
        Artist artist = CreateArtistRequest
                .dtoToEntityMapper()
                .apply(request);
        service.create(artist);
        return Response.created(UriBuilder.fromMethod(ArtistController.class, "getArtist")
                .build(artist.getName())).build();
    }

    @PUT
    @Path("{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putArtist(@PathParam("name") String name, UpdateArtistRequest request) {
        Optional<Artist> artist = service.find(name);

        if (artist.isPresent()) {
            UpdateArtistRequest.dtoToEntityUpdater().apply(artist.get(), request);

            service.update(artist.get());
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{name}")
    @RolesAllowed(UserRoles.ADMIN)
    public Response deleteArtist(@PathParam("name") String name) {
        Optional<Artist> artist = service.find(name);
        if (artist.isPresent()) {
            service.delete(artist.get());
            return Response
                    .noContent()
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }
}
