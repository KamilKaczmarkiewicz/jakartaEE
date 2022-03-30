package paintings.painting.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * JSF view model class in order to not to use entity classes. Represents list of paintings to be displayed.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PaintingsModel implements Serializable {

    /**
     * Represents single painting in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Painting {

        private Long id;

        private String name;

        private int likes;

        private String artist;

        private Long version;

        private LocalDateTime creationDateTime;

        private LocalDateTime lastEditDateTime;

    }

    @Singular
    private List<Painting> paintings;

    public static Function<Collection<paintings.painting.entity.Painting>, PaintingsModel> entityToModelMapper() {
        return paintings -> {
            PaintingsModelBuilder model = PaintingsModel.builder();
            paintings.stream()
                    .map(painting -> Painting.builder()
                            .id(painting.getId())
                            .name(painting.getName())
                            .likes(painting.getLikes())
                            .artist(painting.getArtist().getName())
                            .version((painting.getVersion()))
                            .creationDateTime(painting.getCreationDateTime())
                            .lastEditDateTime(painting.getLastEditDateTime())
                            .build())
                    .forEach(model::painting);
            return model.build();
        };
    }

}
