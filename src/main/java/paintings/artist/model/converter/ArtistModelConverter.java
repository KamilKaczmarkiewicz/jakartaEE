package paintings.artist.model.converter;


import paintings.artist.entity.Artist;
import paintings.artist.model.ArtistModel;
import paintings.artist.service.ArtistService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Faces converter for {@link ArtistModel}. The managed attribute in {@link @FacesConverter} allows the converter
 * to be the CDI bean. In previous version of JSF converters were always created inside JSF lifecycle and where not
 * managed by container that is injection was not possible. As this bean is not annotated with scope the beans.xml
 * descriptor must be present.
 */
@FacesConverter(forClass = ArtistModel.class, managed = true)
public class ArtistModelConverter implements Converter<ArtistModel> {

    /**
     * Service for artists management.
     */
    private ArtistService service;

    @Inject
    public ArtistModelConverter(ArtistService service) {
        this.service = service;
    }

    @Override
    public ArtistModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Optional<Artist> artist = service.find(value);
        return artist.isEmpty() ? null : ArtistModel.entityToModelMapper().apply(artist.get());
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ArtistModel value) {
        return value == null ? "" : value.getName();
    }

}
