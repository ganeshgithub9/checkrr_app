package com.example.checkrr.controller;

import com.example.checkrr.dto.CandidateDTO;
import com.example.checkrr.dto.ReportDTO;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.exceptions.CandidateNotFoundException;
import com.example.checkrr.exceptions.ReportNotFoundException;
import com.example.checkrr.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CandidateService candidateService;

    @MockitoBean
    ResourceHttpRequestHandler handler;

    Logger logger=Logger.getLogger(getClass().getName());
    ObjectMapper objectMapper=new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void givenNotExistCandidateId_WhenGetCandidateGeneralInfoById_ThenReturnsCandidateNotFound() throws Exception{

        when(candidateService.getCandidateGeneralInfoById(anyLong())).thenThrow(new CandidateNotFoundException("Candidate id 9 does not exist"));

        mockMvc.perform(get("/api/v1/candidates/{candidate-id}",9))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Candidate id 9 does not exist"));
    }

    @Test
    void givenExistCandidateId_WhenGetCandidateGeneralInfoById_ThenReturnsCandidateInfo() throws Exception{

        CandidateDTO expectedCandidateDTO=new CandidateDTO();
        expectedCandidateDTO.setId(4L);
        expectedCandidateDTO.setName("rock");
        expectedCandidateDTO.setDriversLicenseNumber("AABK23");
        expectedCandidateDTO.setLocation("Delhi");
        expectedCandidateDTO.setEmail("axy@yy.in");
        expectedCandidateDTO.setDob(LocalDate.of(2002,5,12));
        expectedCandidateDTO.setPhone("2345342");
        expectedCandidateDTO.setZipcode(2342233);
        expectedCandidateDTO.setSocialSecurityNumber("ASS231");
        expectedCandidateDTO.setCreatedAt(LocalDate.now());
        when(candidateService.getCandidateGeneralInfoById(anyLong())).thenReturn(expectedCandidateDTO);

        MvcResult result=mockMvc.perform(get("/api/v1/candidates/{candidate-id}",4))
                .andExpect(status().isOk())
                .andReturn();
        String actualString=result.getResponse().getContentAsString(),expectedString=objectMapper.writeValueAsString(expectedCandidateDTO);
        assertEquals(actualString,expectedString);
    }

    @Test
    void givenNotExistCandidateId_WhenGetReportByCandidateId_ThenReturnsReportNotFound() throws Exception{

        when(candidateService.getReportByCandidateId(anyLong())).thenThrow(new ReportNotFoundException("Report not found for Candidate id 9"));

        mockMvc.perform(get("/api/v1/candidates/{candidate-id}/report",9))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Report not found for Candidate id 9"));
    }

    @Test
    void givenExistCandidateId_WhenGetReportByCandidateId_ThenReturnsReportDetails() throws Exception{

        ReportDTO expectedReportDTO=new ReportDTO();
        expectedReportDTO.setId(2L);
        expectedReportDTO.setAdjudication(Adjudication.PRE_ADVERSE_ACTION);
        expectedReportDTO.setPackageType("HIRE-PRO");
        expectedReportDTO.setAdjudicationStatus(Status.CONSIDER);
        expectedReportDTO.setAdjudicationCreatedAt(LocalDate.now());
        expectedReportDTO.setAdjudicationCompletedAt(LocalDate.now());
        expectedReportDTO.setTurnAroundTime(22);
        when(candidateService.getReportByCandidateId(anyLong())).thenReturn(expectedReportDTO);

        MvcResult result=mockMvc.perform(get("/api/v1/candidates/{candidate-id}/report",2))
                .andExpect(status().isOk())
                .andReturn();
        String actualResult=result.getResponse().getContentAsString(),expectedResult=objectMapper.writeValueAsString(expectedReportDTO);
        assertEquals(actualResult,expectedResult);
    }

    @Test
    void givenInvalidCandidateDetails_WhenCreateCandidate_ThenReturnsValidationErrors() throws Exception{

        String body=new JSONObject()
                .put("name","").put("driversLicenseNumber","DGSK123")
                .put("location","Dublin")
                .put( "email","abcdxyz.com").put("dob","2001-10-12")
                .put("phone","1234567899").put("socialSecurityNumber","1234321")
                .put("zipcode",121212).toString();
        mockMvc.perform(post("/api/v1/candidates")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.email").value("Enter valid email"));
    }

    @Test
    void givenInvalidDateFormat_WhenCreateCandidate_ThenReturnsDateTimeParseError() throws Exception{

        String body=new JSONObject()
                .put("name","rok").put("driversLicenseNumber","DGSK123")
                .put("location","Dublin")
                .put( "email","abcdx@yz.com").put("dob","20011012")
                .put("phone","1234567899").put("socialSecurityNumber","1234321")
                .put("zipcode",121212).toString();
        mockMvc.perform(post("/api/v1/candidates")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String \"20011012\": Failed to deserialize java.time.LocalDate: (java.time.format.DateTimeParseException) Text '20011012' could not be parsed at index 0"));
    }

    @Test
    void givenValidCandidateDetails_WhenCreateCandidate_ThenReturnsSuccessResponse() throws Exception{

        String body=new JSONObject()
                .put("name","Jake").put("driversLicenseNumber","DGSK123")
                .put("location","Dublin")
                .put( "email","abcd@xyz.com").put("dob","2001-10-12")
                .put("phone","1234567899").put("socialSecurityNumber","1234321")
                .put("zipcode",121212).toString();

        when(candidateService.createCandidate(any(CandidateDTO.class))).thenReturn(9L);

        mockMvc.perform(post("/api/v1/candidates")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Created a candidate with id 9"));
    }
}






