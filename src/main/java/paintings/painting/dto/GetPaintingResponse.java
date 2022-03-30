package paintings.painting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import paintings.painting.entity.Painting;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetPaintingResponse {

    private Long id;

    private String name;

    private LocalDate creationDate;

    private String description;

    private int likes;

    private String artistName;


    public static Function<Painting, GetPaintingResponse> entityToDtoMapper() {
        return painting -> GetPaintingResponse.builder()
                .id(painting.getId())
                .name(painting.getName())
                .creationDate(painting.getCreationDate())
                .description(painting.getDescription())
                .likes(painting.getLikes())
                .artistName(painting.getArtist().getName())
                .build();
    }
}
