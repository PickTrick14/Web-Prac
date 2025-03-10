package com.db;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name = "ex_exp_person")
public class ExExpPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expID")
    private Expirience expirience;

    @ManyToOne
    @JoinColumn(name = "personID")
    private Person person;
}