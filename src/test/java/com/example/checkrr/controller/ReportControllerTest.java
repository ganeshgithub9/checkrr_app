package com.example.checkrr.controller;

import com.example.checkrr.dto.CandidateWithReportDTO;
import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    @MockitoBean
    ReportService reportService;

    ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    Page<CandidateWithReportDTO> pageOfCandidateWithReportDTOs;

    @BeforeEach
    void setup(){
        objectMapper.registerModule(new JavaTimeModule());

        CandidateWithReportDTO candidateWithReportDTO1=new CandidateWithReportDTO(1L,"jake","rome", Adjudication.PRE_ADVERSE_ACTION, Status.CONSIDER, LocalDate.now());
        CandidateWithReportDTO candidateWithReportDTO2=new CandidateWithReportDTO(4L,"drake","moscow", Adjudication.PRE_ADVERSE_ACTION, Status.CLEAR, LocalDate.now());
        pageOfCandidateWithReportDTOs=new PageImpl<>(List.of(candidateWithReportDTO1,candidateWithReportDTO2), PageRequest.of(2,2),6);

        when(reportService.getCandidatesWithReports(any(),any(),any(),any())).thenReturn(pageOfCandidateWithReportDTOs);
    }

    @Test
    void given_Name_Adjudication_AdjudicationStatus_PageableObject_WhenGetCandidatesWithReports_ThenReturns_APageOfCandidateWithReportDTOs() throws Exception {

        MvcResult result=mockMvc.perform(get("/api/v1/candidates-with-reports/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.number").value(2))
                .andExpect(jsonPath(("$.first")).value("false"))
                .andExpect(jsonPath(("$.last")).value("true"))
                .andReturn();

        String actualResult=result.getResponse().getContentAsString(),expectedResult=objectMapper.writeValueAsString(pageOfCandidateWithReportDTOs);
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void given_DateRange_WhenGetCandidatesWithReportsByDateRange_ThenReturns_APageOfCandidateWithReportDTOs_InGivenDateRange() throws Exception {

        ExportDTO exportDTO=new ExportDTO(LocalDate.now(),LocalDate.now().plusDays(4));
        String body=objectMapper.writeValueAsString(exportDTO);
        List<CandidateWithReportDTO> list=pageOfCandidateWithReportDTOs.stream().toList();
        when(reportService.getCandidatesWithReportsByDateRange(any())).thenReturn(list);
        MvcResult result=mockMvc.perform(post("/api/v1/candidates-with-reports:export")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        String actualResult=result.getResponse().getContentAsString(),expectedResult=objectMapper.writeValueAsString(list);
        assertEquals(expectedResult,actualResult);
    }
}
