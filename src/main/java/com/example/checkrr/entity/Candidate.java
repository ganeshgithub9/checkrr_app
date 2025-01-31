package com.example.checkrr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String driversLicenseNumber;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private Integer zipcode;

    @Column(nullable = false, unique = true)
    private String socialSecurityNumber;

    @Column(nullable = false)
    private LocalDate createdAt;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "report_id",unique = true)
    private Report report;

    @OneToMany(mappedBy = "candidate")
    List<AdverseAction> adverse_actions=new ArrayList<>();

    @OneToMany(mappedBy = "candidate")
    List<CourtSearch> court_searches=new ArrayList<>();
}

