package com.example.checkrr.controller;

import com.example.checkrr.dto.CourtSearchDTO;
import com.example.checkrr.enums.Status;
import com.example.checkrr.exceptions.CourtSearchNotFoundException;
import com.example.checkrr.projection.CourtSearchProjection;
import com.example.checkrr.service.CourtSearchService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CourtSearchControllerTest {

    @MockitoBean
    CourtSearchService courtSearchService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper=new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void givenNotExistCandidateId_WhenGetCourtSearchDetailsByCandidateId_ThenReturnsNoCourtSearchFound() throws Exception{

        when(courtSearchService.getCourtSearchDetailsByCandidateId(anyLong())).thenThrow(new CourtSearchNotFoundException("No CourtSearch found for the candidate id 9"));

        mockMvc.perform(get("/api/v1/candidates/{candidate-id}/court-searches",9))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No CourtSearch found for the candidate id 9"));
    }

    @Test
    void givenNotExistCandidateId_WhenCreateCourtSearch_ThenReturnsSQLIntegerityValidationException() throws Exception{

        String body=new JSONObject()
                .put( "name","Global Watchlist")
                        .put("status","CONSIDER").toString();

        when(courtSearchService.createCourtSearch(anyLong(),any(CourtSearchDTO.class))).thenThrow(new SQLException("could not execute statement [Cannot add or update a child row: a foreign key constraint fails (`checkrr`.`court_search`, CONSTRAINT `FK__court_search__candidate__id` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`))] [/* insert for com.example.checkrr.entity.CourtSearch */insert into court_search (candidate_id,name,reported_at,status) values (?,?,?,?)]; SQL [/* insert for com.example.checkrr.entity.CourtSearch */insert into court_search (candidate_id,name,reported_at,status) values (?,?,?,?)]; constraint [null]"));

        mockMvc.perform(post("/api/v1/candidates/{candidate-id}/court-searches",9)
                        .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("could not execute statement [Cannot add or update a child row: a foreign key constraint fails (`checkrr`.`court_search`, CONSTRAINT `FK__court_search__candidate__id` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`))] [/* insert for com.example.checkrr.entity.CourtSearch */insert into court_search (candidate_id,name,reported_at,status) values (?,?,?,?)]; SQL [/* insert for com.example.checkrr.entity.CourtSearch */insert into court_search (candidate_id,name,reported_at,status) values (?,?,?,?)]; constraint [null]"));
    }

    @Test
    void givenValidCandidateId_WhenCreateCourtSearch_ThenReturnsSuccessResponse() throws Exception{

        String body=new JSONObject()
                .put( "name","Global Watchlist")
                .put("status","CONSIDER").toString();
        when(courtSearchService.createCourtSearch(anyLong(),any(CourtSearchDTO.class))).thenReturn("Created CourtSearch with id 9");

        mockMvc.perform(post("/api/v1/candidates/{candidate-id}/court-searches",9)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Created CourtSearch with id 9"));
    }

    @Test
    void givenExistCandidateId_WhenGetCourtSearchDetailsByCandidateId_ThenReturnsCourtSearchDetails() throws Exception{
        List<CourtSearchProjection> courtSearchProjections=new ArrayList<>();
        courtSearchProjections.add(new CourtSearchProjection() {
            @Override
            public Long getId() {
                return 3L;
            }

            @Override
            public String getName() {
                return "Country Criminal";
            }

            @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
            @Override
            public LocalDate getReportedAt() {
                return LocalDate.now();
            }

            @Override
            public Status getStatus() {
                return Status.CONSIDER;
            }
        });
        when(courtSearchService.getCourtSearchDetailsByCandidateId(anyLong())).thenReturn(courtSearchProjections);

        MvcResult result=mockMvc.perform(get("/api/v1/candidates/{candidate-id}/court-searches",3))
                .andExpect(status().isOk())
                .andReturn();
        String expectedResult=objectMapper.writeValueAsString(courtSearchProjections),actualResult=result.getResponse().getContentAsString();
        assertEquals(expectedResult,actualResult);
    }
}


