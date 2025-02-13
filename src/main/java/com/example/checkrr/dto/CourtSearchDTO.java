package com.example.checkrr.dto;

import com.example.checkrr.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CourtSearchDTO {
    @NotBlank(message = "CourtSearch name is required")
    private String name;
    private Status status;
}
