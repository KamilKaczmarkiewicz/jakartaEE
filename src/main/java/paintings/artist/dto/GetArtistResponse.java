package paintings.artist.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import paintings.artist.entity.Artist;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetArtistResponse {

    private String name;
    private LocalDate birthDate;
    private boolean isAlive;

    public static Function<Artist, GetArtistResponse> entityToDtoMapper() {
        return artist -> {
            GetArtistResponse.GetArtistResponseBuilder response = GetArtistResponse.builder();
            response.name(artist.getName())
                    .birthDate(artist.getBirthDate())
                    .isAlive(artist.isAlive());
            return response.build();
        };
    }
}
