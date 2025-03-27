package com.db.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "institution")
    private String institution;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "endYear")
    private Integer endYear;

    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;
}