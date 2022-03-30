package paintings.painting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import paintings.painting.dto.UpdatePaintingRequest;
import paintings.painting.entity.Painting;
import paintings.painting.entity.Painting;

import java.time.LocalDate;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdatePaintingRequest {

    private String name;
    private int likes;

    public static BiFunction<Painting, UpdatePaintingRequest, Painting> dtoToEntityUpdater() {
        return (painting, request) -> {
            painting.setName(request.getName());
            painting.setLikes(request.getLikes());
            return painting;
        };
    }
}
