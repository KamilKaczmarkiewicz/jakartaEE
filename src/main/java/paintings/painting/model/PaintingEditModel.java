package paintings.painting.model;

import lombok.*;
import paintings.artist.entity.Artist;
import paintings.artist.model.converter.ArtistModelConverter;
import paintings.painting.entity.Painting;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents single painting to be edited. Includes
 * only fields which can be edited after painting creation.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PaintingEditModel {

    private String name;

    private String description;

    private int likes;

    private Artist artist;

    public static Function<Painting, PaintingEditModel> entityToModelMapper() {
        return painting -> PaintingEditModel.builder()
                .name(painting.getName())
                .description(painting.getDescription())
                .likes(painting.getLikes())
                .artist(painting.getArtist())
                .build();
    }

    public static BiFunction<Painting, PaintingEditModel, Painting> modelToEntityUpdater() {
        return (painting, request) -> {
            painting.setName(request.getName());
            painting.setDescription(request.getDescription());
            painting.setLikes(request.getLikes());
            painting.setArtist(request.getArtist());
            return painting;
        };
    }

}
