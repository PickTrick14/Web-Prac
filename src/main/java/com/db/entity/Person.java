package com.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city")
    private City city;

    @Column(name = "fullName", nullable = false)
    private String name;

    @Column(name = "contactInfo")
    private String contactInfo;

    @Column(name = "isSearching")
    private boolean isSearching;

    @Column(name = "age")
    private int age;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Account account;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Education> educations;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Resume> resumes;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Response> responses;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<ExExpPerson> exExpPersons;
}
