package com.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "application")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "applicationDate", nullable = false)
    private LocalDate applicationDate;


    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "vacancyID")
    private Vacancy vacancy;

    @Column(name = "status", nullable = false)
    private String status;
}