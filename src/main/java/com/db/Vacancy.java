package com.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
@Table(name = "vacancy")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private Integer salary;

    private String requirements;

    @ManyToOne
    @JoinColumn(name = "companyID")
    private Company company;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL)
    private List<Response> responses;
}