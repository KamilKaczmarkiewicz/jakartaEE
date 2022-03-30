package paintings.artist.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import paintings.artist.entity.Artist;

import java.time.LocalDate;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateArtistRequest {

    private LocalDate birthDate;
    private boolean isAlive;

    public static BiFunction<Artist, UpdateArtistRequest, Artist> dtoToEntityUpdater() {
        return (artist, request) -> {
            artist.setBirthDate(request.getBirthDate());
            artist.setAlive(request.isAlive());
            return artist;
        };
    }
}
