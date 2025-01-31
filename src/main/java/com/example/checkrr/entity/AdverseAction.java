package com.example.checkrr.entity;

import com.example.checkrr.enums.AdverseActionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "adverse_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdverseAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate preNoticeDate;


    private LocalDate postNoticeDate;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    private AdverseActionStatus status;
}

