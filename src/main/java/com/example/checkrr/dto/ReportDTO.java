package com.example.checkrr.dto;

import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long id;


    private Adjudication adjudication;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate adjudicationCreatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate adjudicationCompletedAt;


    private Status adjudicationStatus;


    private String packageType;

    private Integer turnAroundTime;
}
