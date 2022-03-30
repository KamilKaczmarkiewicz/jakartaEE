package paintings.painting.controller;

import paintings.painting.dto.CreatePaintingRequest;
import paintings.painting.dto.GetPaintingsResponse;
import paintings.painting.dto.GetPaintingResponse;
import paintings.painting.entity.Painting;
import paintings.painting.service.PaintingService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Optional;

@Path("/artists/{name}/paintings")
public class ArtistPaintingController {

    private PaintingService paintingService;

    public ArtistPaintingController() {
    }

    @EJB
    public void setPaintingService(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaintings(@PathParam("name") String artist) {
        Optional<List<Painting>> paintings = paintingService.findAllByArtist(artist);
        if (paintings.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(GetPaintingsResponse.entityToDtoMapper().apply(paintings.get())).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPainting(@PathParam("name") String artist, @PathParam("id") Long id) {
        Optional<Painting> painting = paintingService.findByArtist(artist, id);
        if (painting.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(GetPaintingResponse.entityToDtoMapper().apply(painting.get())).build();
        }
    }

}
