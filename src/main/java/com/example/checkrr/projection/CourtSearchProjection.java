package com.example.checkrr.projection;

import com.example.checkrr.enums.Status;

import java.time.LocalDate;

public interface CourtSearchProjection {
    Long getId();
    String getName();
    LocalDate getReportedAt();
    Status getStatus();
}
