package com.db;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name = "person_experience")
public class ExExpPerson {
    @Id
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "experienceid")
    private Expirience expirience;

    @Id
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "personID")
    private Person person;
}