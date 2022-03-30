package paintings.artist.model;

import lombok.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents list of artists to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ArtistsModel implements Serializable {

    /**
     * Represents single artist in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Artist {

        /**
         * Unique id identifying artist.
         */
        private String name;


    }

    /**
     * Name of the selected artists.
     */
    @Singular
    private List<Artist> artists;

    /**
     * @return mapper for convenient converting entity object to model object
     */
    public static Function<Collection<paintings.artist.entity.Artist>, ArtistsModel> entityToModelMapper() {
        return artists -> {
            ArtistsModelBuilder model = ArtistsModel.builder();
            artists.stream()
                    .map(artist -> Artist.builder()
                            .name(artist.getName())
                            .build())
                    .forEach(model::artist);
            return model.build();
        };
    }

}
