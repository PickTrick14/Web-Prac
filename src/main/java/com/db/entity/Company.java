package com.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Vacancy> vacancies;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Expirience> employers;
}