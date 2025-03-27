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

    @JoinColumn(name = "accountID")
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
}
