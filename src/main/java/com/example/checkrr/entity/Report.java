package com.example.checkrr.entity;

import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Adjudication adjudication;


    private LocalDate adjudicationCreatedAt;


    private LocalDate adjudicationCompletedAt;

    @Enumerated(EnumType.STRING)
    private Status adjudicationStatus;


    private String packageType;

    private Integer turnAroundTime;

    @OneToOne(mappedBy = "report")
    private Candidate candidate;
}

