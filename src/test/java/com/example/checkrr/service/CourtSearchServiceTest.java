package com.example.checkrr.service;

import com.example.checkrr.dto.CourtSearchDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.CourtSearch;
import com.example.checkrr.enums.Status;
import com.example.checkrr.exceptions.CourtSearchNotFoundException;
import com.example.checkrr.projection.CourtSearchProjection;
import com.example.checkrr.repository.CourtSearchRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class CourtSearchServiceTest {

    @Mock
    private CustomCandidateService candidateService;
    @Mock
    private CourtSearchRepository courtSearchRepository;
    @Mock
    private ModelMapper mapper;

    @InjectMocks
    CustomCourtSearchService courtSearchService;

    Candidate candidate;

    CourtSearch courtSearch;
    CourtSearchDTO courtSearchDTO;
    List<CourtSearchProjection> courtSearchProjections;

    @BeforeEach
    void setup(){
        openMocks(this);

        candidate=new Candidate();
        candidate.setId(2L);
        candidate.setEmail("abb@abc.us");
        candidate.setName("man");
        candidate.setLocation("delhi");

        courtSearch=new CourtSearch();
        courtSearch.setId(5L);
        courtSearch.setName("Offender");
        courtSearch.setStatus(Status.CLEAR);

        courtSearchDTO=new CourtSearchDTO("Offender",Status.CLEAR);

        courtSearchProjections=new ArrayList<>();
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
    }

    @Test
    void givenCandidateIdAndCourtSearchDTO_WhenCreateCourtSearch_ThenReturnsSuccessResponse() throws SQLException {

        when(candidateService.getReferenceByCandidateId(anyLong())).thenReturn(candidate);
        when(mapper.map(any(CourtSearchDTO.class), eq(CourtSearch.class))).thenReturn(courtSearch);
        when(courtSearchRepository.save(any(CourtSearch.class))).thenReturn(courtSearch);

        String actualResult=courtSearchService.createCourtSearch(2L,courtSearchDTO);

        assertEquals("Created CourtSearch with id 5",actualResult);

    }

    @Test
    void givenCandidateId_WhenGetCourtSearchDetailsByCandidateId_ThenReturnsCourtSearchProjections() throws CourtSearchNotFoundException {

        when(courtSearchRepository.getCourtSearchDetailsByCandidateId(anyLong())).thenReturn(Optional.ofNullable(courtSearchProjections));

        List<CourtSearchProjection> actualResult=courtSearchService.getCourtSearchDetailsByCandidateId(2L);

        List<CourtSearchProjection> courtSearchProjections1=new ArrayList<>();
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
        assertEquals(courtSearchProjections.size(),actualResult.size());
        assert(courtSearchProjections.equals(actualResult));
        assert(!courtSearchProjections1.equals(actualResult));

    }
}
