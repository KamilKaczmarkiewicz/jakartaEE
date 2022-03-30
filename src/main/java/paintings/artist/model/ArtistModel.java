package paintings.artist.model;

import lombok.*;
import paintings.artist.entity.Artist;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JSF view model class in order to not to use entity classes. Represents single profession to be displayed or selected.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ArtistModel {

    private String name;
    private LocalDate birthDate;
    private boolean isAlive;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Artist, ArtistModel> entityToModelMapper() {
        return artist -> ArtistModel.builder()
                .name(artist.getName())
                .birthDate(artist.getBirthDate())
                .isAlive(artist.isAlive())
                .build();
    }
}
