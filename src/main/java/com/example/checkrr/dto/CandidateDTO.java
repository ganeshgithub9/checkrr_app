package com.example.checkrr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "DriversLicenseNumber is required")
    private String driversLicenseNumber;

    @NotBlank(message = "Location is required")
    private String location;

    @Email(message = "Enter valid email")
    private String email;

    @NotNull(message = "DOB is required and past")
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotBlank(message = "Mobile number is required")
    private String phone;

    @Min(value = 100000,message = "Enter valid zipcode")
    private Integer zipcode;


    @NotBlank(message = "Social security number is required")
    private String socialSecurityNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
