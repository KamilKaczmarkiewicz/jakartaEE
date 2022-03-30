package paintings.painting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import paintings.artist.entity.Artist;
import paintings.painting.entity.Painting;
import paintings.user.entity.User;

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
public class CreatePaintingRequest {

    private String name;

    private LocalDate creationDate;

    private String description;

    private int likes;

    private String artist;


    public static Function<CreatePaintingRequest, Painting> dtoToEntityMapper(
            Function<String, Artist> artistFunction,
            Supplier<User> userSupplier
    ) {
        return request -> Painting.builder()
                .name(request.getName())
                .creationDate(request.getCreationDate())
                .description(request.getDescription())
                .likes(request.getLikes())
                .artist(artistFunction.apply(request.getArtist()))
                .user(userSupplier.get())
                .build();
    }
}
