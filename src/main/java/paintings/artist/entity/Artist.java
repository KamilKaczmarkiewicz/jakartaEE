package paintings.artist.entity;

import lombok.*;
import paintings.painting.entity.Painting;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Entity class for game characters' professions (classes). Describes name of the profession and skills available on
 * different levels.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "artists")
public class Artist implements Serializable {

    @Id
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private boolean isAlive;

    @ToString.Exclude//It's common to exclude lists from toString
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "artist", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Painting> paintings;

}
