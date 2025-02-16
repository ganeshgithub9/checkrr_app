package com.example.checkrr.controller;

import com.example.checkrr.dto.AdverseActionDTO;
import com.example.checkrr.dto.AdverseActionResponseDTO;
import com.example.checkrr.dto.EmailMetaData;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.service.AdverseActionService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdverseActionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AdverseActionService adverseActionService;

    @Test
    void givenValidCandidateIdAndAdverseActionDTO_WhenCreateAdverseAction_ThenReturnsSuccessResponse() throws Exception{

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

    @Test
    void givenValidCandidateIdAndAdverseActionDTOAndEmailMetaDataAndMultipartFiles_WhenCreateAdverseAction_ThenReturnsSuccessResponse() throws Exception{


        MockMultipartFile file1 = new MockMultipartFile("files", "test1.txt", "text/plain", "Dummy file content 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("files", "test2.txt", "text/plain", "Dummy file content 2".getBytes(StandardCharsets.UTF_8));
        String emailMetadataJson = "{\"senderId\":1,\"receiverId\":2,\"mailSubject\":\"Pre-adverse action notice\", \"bodyInHtml\":\"<div></div>\"}";
        String adjudicationJson = "{\"adjudication\":\"ENGAGE\",\"status\":\"CLEAR\",\"noticeDays\":4}";
        MockMultipartFile emailMetaDataMultipartFile = new MockMultipartFile("emailMetaData", "", MediaType.APPLICATION_JSON_VALUE, emailMetadataJson.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile adjudicationDataMultipartFile = new MockMultipartFile("adjudicationData", "", MediaType.APPLICATION_JSON_VALUE, adjudicationJson.getBytes(StandardCharsets.UTF_8));


        when(adverseActionService.createAdverseActionWithMailAndAttachments(anyLong(),any(AdverseActionDTO.class),any(EmailMetaData.class),any(MultipartFile[].class))).thenReturn("Adverse action created with id 9");

        mockMvc.perform(multipart("/api/v2/candidates/{candidate-id}/adverse-actions",9)
                        .file(file1)
                        .file(file2)
                        .file(emailMetaDataMultipartFile)
                        .file(adjudicationDataMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Adverse action created with id 9"));
    }
}



