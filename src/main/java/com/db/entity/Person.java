package com.db.entity;

import lombok.*;

import javax.persistence.*;

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
    @JoinColumn(name = "city", nullable = false)
    private City city;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contactInfo")
    private String contactInfo;

    @Column(name = "isSearching")
    private boolean isSearching;

    @Column(name = "age")
    private int age;

    @OneToOne
    @JoinColumn(name = "accountID", nullable = false)
    private Account account;
}
