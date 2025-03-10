package com.db;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    private Long id;

    private String email;

    private String password;

    private boolean isEmployee;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")  // Ссылка на Person.id
    private Person person;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id") // Ссылка на Company.id
    private Company company;
}