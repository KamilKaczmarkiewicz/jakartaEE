package paintings.painting.model;

import lombok.*;
import paintings.artist.entity.Artist;
import paintings.painting.entity.Painting;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents single painting to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PaintingModel {

    private String name;

    private LocalDate creationDate;

    private String description;

    private int likes;

    private String artist;

    private Long version;

    private LocalDateTime creationDateTime;

    private LocalDateTime lastEditDateTime;

    public static Function<Painting, PaintingModel> entityToModelMapper() {
        return painting -> PaintingModel.builder()
                .name(painting.getName())
                .creationDate(painting.getCreationDate())
                .description(painting.getDescription())
                .likes(painting.getLikes())
                .artist(painting.getArtist().getName())
                .version((painting.getVersion()))
                .creationDateTime(painting.getCreationDateTime())
                .lastEditDateTime(painting.getLastEditDateTime())
                .build();
    }
}
