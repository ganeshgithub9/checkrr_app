package com.example.checkrr.controller;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.service.AdverseActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdverseActionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AdverseActionService adverseActionService;

    @Test
    void givenValidCandidateId_WhenCreateAdverseAction_ThenReturnsSuccessResponse() throws Exception{

        String body=new JSONObject()
                .put( "adjudication","ENGAGE")
                .put("status","CLEAR")
                .put("noticeDays", 5).toString();
        when(adverseActionService.createAdverseAction(anyLong(),any(AdverseActionDTO.class))).thenReturn("Adverse action created with id 9");

        mockMvc.perform(post("/api/v1/candidates/{candidate-id}/adverse-actions",9)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Adverse action created with id 9"));
    }

    @Test
    void given_Name_Status_PageableObject_WhenGetAdverseActions_ThenReturnsAPageOfAdverseActionResponseDTOs() throws Exception {
        AdverseActionResponseDTO adverseActionResponseDTO1=new AdverseActionResponseDTO(3L, LocalDate.now(),LocalDate.now().plusDays(3), AdverseActionStatus.CANCELED,"paul");
        AdverseActionResponseDTO adverseActionResponseDTO2=new AdverseActionResponseDTO(6L, LocalDate.now(),LocalDate.now().plusDays(11), AdverseActionStatus.DISPUTE,"jake");

        Pageable pageable= PageRequest.of(0,10);
        Page<AdverseActionResponseDTO> pageOfAdverseActionResponseDTOs=new PageImpl<>(List.of(adverseActionResponseDTO1,adverseActionResponseDTO2),pageable,15);
        ObjectMapper objectMapper=new ObjectMapper();objectMapper.registerModule(new JavaTimeModule());

        when(adverseActionService.getAdverseActions(any(),any(),any())).thenReturn(pageOfAdverseActionResponseDTOs);

        MvcResult result=mockMvc.perform(get("/api/v1/adverse-actions/filter")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath(("$.first")).value("true"))
                .andReturn();

        String actualResult=result.getResponse().getContentAsString(),expectedResult=objectMapper.writeValueAsString(pageOfAdverseActionResponseDTOs);
        assertEquals(expectedResult,actualResult);

    }
}



