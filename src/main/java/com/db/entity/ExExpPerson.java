package com.db.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "Person_Experience")
public class ExExpPerson {
    @Embeddable
    @EqualsAndHashCode
    public static class CompositeKey implements Serializable {
        @Column(name = "experienceid")
        private Long experienceId;

        @Column(name = "personID")
        private Long personId;
    }

    @EmbeddedId
    private CompositeKey compositeKey;

    @ManyToOne
    @MapsId("experienceId")
    @JoinColumn(name = "experienceid", referencedColumnName = "id")
    private Experience experience;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "personID", referencedColumnName = "id")
    private Person person;
}