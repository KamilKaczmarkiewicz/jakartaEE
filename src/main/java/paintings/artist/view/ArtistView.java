package paintings.artist.view;

import lombok.Getter;
import lombok.Setter;
import paintings.artist.entity.Artist;
import paintings.artist.model.ArtistModel;
import paintings.artist.service.ArtistService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * View bean for rendering single artists.
 */
@RequestScoped
@Named
public class ArtistView implements Serializable {

    private final ArtistService service;

    @Setter
    @Getter
    private String name;

    @Getter
    private ArtistModel artist;

    @Inject
    public ArtistView(ArtistService service) {
        this.service = service;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached wihitn
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Artist> artist = service.find(name);
        if (artist.isPresent()) {
            this.artist = ArtistModel.entityToModelMapper().apply(artist.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Artist not found");
        }
    }

    
}
