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
@Table(name = "response")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "responseDate", nullable = false)
    private LocalDate responseDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "vacancyID")
    private Vacancy vacancy;
}