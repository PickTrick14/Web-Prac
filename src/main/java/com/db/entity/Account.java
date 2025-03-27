package com.db.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private Long password;

    @Column(name = "isEmployer")
    private boolean isEmployer;
}