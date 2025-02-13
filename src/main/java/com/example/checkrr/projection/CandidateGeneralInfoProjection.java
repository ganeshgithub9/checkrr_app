package com.example.checkrr.projection;



import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface CandidateGeneralInfoProjection {
     Long getId();
     String getName();

     @Value("#{target.driversLicenseNumber}")
     String getDriversLicenseNumber();

     @Value("#{target.location}")
     String getLocation();
     @Value("#{target.email}")
     String getEmail();
     @Value("#{target.dob}")
     String getDob();
     @Value("#{target.phone}")
     String getPhone();
     @Value("#{target.zipcode}")
     Integer getZipcode();
     @Value("#{target.socialSecurityNumber}")
     String getSocialSecurityNumber();
     @Value("#{target.createdAt}")
     LocalDate getCreatedAt();
}
