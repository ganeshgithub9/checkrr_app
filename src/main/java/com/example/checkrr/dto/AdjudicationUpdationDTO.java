package com.example.checkrr.dto;

import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class AdjudicationUpdationDTO {
    Long reportId;
    LocalDate createdAt;
    LocalDate completedAt;
    Adjudication adjudication;
    Status status;
}
