package com.example.checkrr.service;


import com.example.checkrr.dto.AdjudicationUpdationDTO;
import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.repository.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


@Slf4j
@SpringBootTest
class ReportServiceTest {

    @Mock
    ReportRepository reportRepository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    CustomReportService reportService;

    Report report;

    AdjudicationUpdationDTO adjudicationUpdationDTO;

    Pageable pageable;

    Page<Report> pageOfReports;

    ObjectMapper objectMapper;
    Logger logger=Logger.getLogger(ReportServiceTest.class.getName());

    @BeforeEach
    void setup(){

        objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

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

        pageable=PageRequest.of(0,5);
        pageOfReports=new PageImpl<>(List.of(report,new Report(5L,Adjudication.ENGAGE,LocalDate.now(),LocalDate.now().plusDays(1),Status.CLEAR,"EMPLOYEE_PRO",12,null)),pageable,12);
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


    @Test
    void given_Name_Adjudication_AdjudicationStatus_PageableObject_WhenGetCandidatesWithReports_ThenReturns_APageOfCandidateWithReportDTOs() throws Exception {


        Page<CandidateWithReportDTO> expectedPageOfCandidateWithReportDTOs= pageOfReports.map(element -> modelMapper.map(element, CandidateWithReportDTO.class));
        when(reportRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageOfReports);

        Page<CandidateWithReportDTO> actualPageOfCandidateWithReportDTOs=reportService.getCandidatesWithReports("james",null,null,pageable);


        String actualResult=objectMapper.writeValueAsString(actualPageOfCandidateWithReportDTOs),expectedResult=objectMapper.writeValueAsString(expectedPageOfCandidateWithReportDTOs);
        assertEquals(expectedResult,actualResult);
        logger.info(expectedResult);
        logger.info(actualResult);
        assertEquals(expectedPageOfCandidateWithReportDTOs.getTotalElements(),actualPageOfCandidateWithReportDTOs.getTotalElements());
        assertEquals(expectedPageOfCandidateWithReportDTOs.getNumber(),actualPageOfCandidateWithReportDTOs.getNumber());
    }

    @Test
    void given_DateRange_WhenGetCandidatesWithReportsByDateRange_ThenReturns_APageOfCandidateWithReportDTOs_InGivenDateRange() throws Exception {

        ExportDTO exportDTO=new ExportDTO(LocalDate.now(),LocalDate.now().plusDays(4));
        List<Report> reportList=pageOfReports.stream().toList();
        List<CandidateWithReportDTO> expectedCandidateWithReportDTOList=reportList.stream().map(report1 -> modelMapper.map(report1,CandidateWithReportDTO.class)).toList();

        when(reportRepository.findAll(any(Specification.class))).thenReturn(reportList);

        List<CandidateWithReportDTO> actualCandidateWithReportDTOList=reportService.getCandidatesWithReportsByDateRange(exportDTO);

        String actualResult=objectMapper.writeValueAsString(actualCandidateWithReportDTOList),expectedResult=objectMapper.writeValueAsString(expectedCandidateWithReportDTOList);
        logger.info(expectedResult);
        logger.info(actualResult);
        assertEquals(expectedResult,actualResult);
        assertEquals(expectedCandidateWithReportDTOList.size(),actualCandidateWithReportDTOList.size());
        assertEquals(expectedCandidateWithReportDTOList.get(0).getAdjudication(),actualCandidateWithReportDTOList.get(0).getAdjudication());
    }

}
