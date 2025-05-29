package com.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "wanted_position", nullable = false)
    private String wantedPosition;

    @Column(name = "wanted_salary", nullable = false)
    private Integer wantedSalary;

    @Column(name = "skills")
    private String skills;

    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;
}