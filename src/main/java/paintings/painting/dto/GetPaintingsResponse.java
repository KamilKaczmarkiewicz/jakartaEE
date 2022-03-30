package paintings.painting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetPaintingsResponse {

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
    }

    /**
     * Name of the selected paintings.
     */
    @Singular
    private List<Painting> paintings;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<paintings.painting.entity.Painting>, GetPaintingsResponse> entityToDtoMapper() {
        return paintings -> {
            GetPaintingsResponseBuilder response = GetPaintingsResponse.builder();
            paintings.stream()
                    .map(painting -> Painting.builder()
                            .id(painting.getId())
                            .name(painting.getName())
                            .likes(painting.getLikes())
                            .build())
                    .forEach(response::painting);
            return response.build();
        };
    }
}
