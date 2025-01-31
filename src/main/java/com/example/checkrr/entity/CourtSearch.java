package com.example.checkrr.entity;

import com.example.checkrr.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "court_search")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id",nullable = false)
    private Candidate candidate;

    @Column(nullable = false)
    private LocalDate reportedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String name;
}

