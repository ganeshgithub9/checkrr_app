package com.example.checkrr.controller;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.service.AdverseActionService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}



