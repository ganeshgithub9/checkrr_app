package com.example.checkrr.configuration;

import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = BeansConfigurer.class)
class ModelMapperConfigTest {
    @Autowired
    ModelMapper modelMapper;

    @Test
    void givenSkipNullCondition_WhenMapsSourceToDestination_ShouldSkipNullMappings(){
        Report report=new Report(5L, Adjudication.ENGAGE, LocalDate.now(),LocalDate.now().plusDays(1), Status.CLEAR,"EMPLOYEE_PRO",12,null);
        CandidateWithReportDTO candidateWithReportDTO=modelMapper.map(report, CandidateWithReportDTO.class);
        assertNull(report.getCandidate(),"Candidate should be null to check skip null condition");
        assertNull(candidateWithReportDTO.getCandidateId(),"Candidate Id should be null as no candidate entity is passed to report");
        assertNull(candidateWithReportDTO.getName(),"Candidate name should be null as no candidate entity is passed to report");
    }
}
