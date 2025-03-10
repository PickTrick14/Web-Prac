package com.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String name;

    private String contactInfo;

    @Column(name = "isSearching")
    private boolean isSearching;

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
