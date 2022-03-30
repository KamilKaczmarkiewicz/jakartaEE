package paintings.artist.view;

import paintings.artist.entity.Artist;
import paintings.artist.model.ArtistsModel;
import paintings.artist.service.ArtistService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Optional;

/**
 * View bean for rendering list of artists.
 */
@RequestScoped
@Named
public class ArtistList implements Serializable {

    /**
     * Service for managing artists.
     */
    private final ArtistService service;

    /**
     * Artists list exposed to the view.
     */
    private ArtistsModel artists;

    @Inject
    public ArtistList(ArtistService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all artists
     */
    public ArtistsModel getArtists() {
        if (artists == null) {
            artists = ArtistsModel.entityToModelMapper().apply(service.findAll());
        }
        return artists;
    }

    /**
     * Action for clicking delete action.
     *
     * @param artist artist to be removed
     * @return navigation case to list_artists
     */
    public String deleteAction(ArtistsModel.Artist artist) {
        Optional<Artist> a = service.find(artist.getName());
        if (a.isPresent()) {
            service.delete(a.get());
        }
        return "artist_list?faces-redirect=true";
    }

}
