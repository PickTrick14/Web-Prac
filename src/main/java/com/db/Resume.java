package com.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name = "resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String wantedPosition;

    private Integer wantedSalary;

    private String skills;

    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<ExExpPerson> exExpPersons;
}