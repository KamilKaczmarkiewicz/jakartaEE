package paintings.painting.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import paintings.artist.entity.Artist;
import paintings.entity.VersionAndCreationDateAuditable;
import paintings.user.entity.User;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "paintings")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Painting extends VersionAndCreationDateAuditable implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    private String description;

    private int likes;

    @ManyToOne
    @JoinColumn(name = "artist")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

}
