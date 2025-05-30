package com.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "vacancy")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Column(name = "requirements")
    private String requirements;

    @ManyToOne
    @JoinColumn(name = "companyID")
    private Company company;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL)
    private List<Response> responses;
}