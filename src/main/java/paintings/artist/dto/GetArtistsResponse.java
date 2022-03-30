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
public class GetArtistsResponse {

    @Singular
    private List<String> artists;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<Artist>, GetArtistsResponse> entityToDtoMapper() {
        return artists -> {
            GetArtistsResponse.GetArtistsResponseBuilder response = GetArtistsResponse.builder();
            artists.stream()
                    .map(Artist::getName)
                    .forEach(response::artist);
            return response.build();
        };
    }
}
