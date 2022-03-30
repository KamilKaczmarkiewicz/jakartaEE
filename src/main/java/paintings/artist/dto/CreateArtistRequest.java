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
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateArtistRequest {


    private String name;
    private LocalDate birthDate;
    private boolean isAlive;

    public static Function<CreateArtistRequest, Artist> dtoToEntityMapper() {
        return request -> Artist.builder()
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .isAlive(request.isAlive())
                .build();
    }
}
