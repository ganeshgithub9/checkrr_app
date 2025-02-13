package com.example.checkrr.dto;

import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdverseActionDTO {
    @Enumerated
    Adjudication adjudication;
    @Enumerated
    Status status;
    @Min(1)
    Integer noticeDays;
}
