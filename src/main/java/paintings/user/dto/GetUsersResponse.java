package paintings.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import paintings.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET users response. Contains user names of users in the system. User name ios the same as login.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUsersResponse {

    /**
     * List of all user names.
     */
    @Singular
    private List<String> users;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<User>, GetUsersResponse> entityToDtoMapper() {
        return characters -> {
            GetUsersResponse.GetUsersResponseBuilder response = GetUsersResponse.builder();
            characters.stream()
                    .map(User::getLogin)
                    .forEach(response::user);
            return response.build();
        };
    }
}
