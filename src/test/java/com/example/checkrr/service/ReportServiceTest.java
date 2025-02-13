package com.example.checkrr.service;


import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class ReportServiceTest {

    @Mock
    ReportRepository reportRepository;

    @InjectMocks
    CustomReportService reportService;

    Report report;

    AdjudicationUpdationDTO adjudicationUpdationDTO;

    @BeforeEach
    void setup(){
        openMocks(this);
        report=new Report();
        report.setAdjudicationStatus(Status.CONSIDER);
        report.setId(4L);
        report.setAdjudication(Adjudication.PRE_ADVERSE_ACTION);
        report.setPackageType("EmployeePRO");
        report.setAdjudicationCreatedAt(LocalDate.now());

        adjudicationUpdationDTO=new AdjudicationUpdationDTO();
        adjudicationUpdationDTO.setReportId(3L);
        adjudicationUpdationDTO.setCompletedAt(LocalDate.now());
        adjudicationUpdationDTO.setStatus(Status.CONSIDER);
    }

    @Test
    void givenReportId_WhenGetReferenceById_thenReturnsReport(){
        when(reportRepository.getReferenceById(anyLong())).thenReturn(report);

        Report actualReport=reportService.getReferenceById(6L);
        assertNotNull(actualReport);
        assert(actualReport.getId().equals(report.getId()));
        assertEquals(actualReport.getAdjudication(),report.getAdjudication());
        assertEquals(actualReport.getPackageType(),report.getPackageType());
        assert(actualReport.getAdjudicationCreatedAt().equals(report.getAdjudicationCreatedAt()));
    }

    @Test
    void givenReport_WhenSaveReport_thenReturnsSuccessResponse(){
        reportService.saveReport(report);
        verify(reportRepository,times(1)).save(report);
    }

    @Test
    void givenAdjudicationDetails_WhenUpdateAdjudicationDetails_thenReturnsSuccessResponse(){
        when(reportRepository.updateAdjudicationDetails(adjudicationUpdationDTO)).thenReturn(1);
        int actual=reportService.updateAdjudicationDetails(adjudicationUpdationDTO);
        assertEquals(1, actual);
    }
}
