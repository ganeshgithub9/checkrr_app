package com.example.checkrr.specification;

import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import com.example.checkrr.specifications.ReportSpecification;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class ReportSpecificationTest {


    @Mock
    private Root<Report> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Join<Candidate, Report> candidateJoin;

    @Mock
    private Path<String> candidateNamePath;


    @Mock
    private Path<String> adjudicationPath;

    @Mock
    private Path<String> adjudicationStatusPath;

    @Mock
    private Path<LocalDate> datePath;

    @Mock
    Predicate predicate;

    @BeforeEach
    void setUp(){
        openMocks(this);

        doReturn(candidateJoin).when(root).join("candidate",JoinType.INNER);
        doReturn(candidateNamePath).when(candidateJoin).get("name");
        doReturn(candidateNamePath).when(criteriaBuilder).lower(candidateNamePath);
        doReturn(predicate).when(criteriaBuilder).like(candidateNamePath,"%ra%");
        doReturn(adjudicationPath).when(root).get("adjudication");
        doReturn(adjudicationStatusPath).when(root).get("adjudicationStatus");
        doReturn(predicate).when(criteriaBuilder).equal(adjudicationPath,Adjudication.PRE_ADVERSE_ACTION.name());
        doReturn(predicate).when(criteriaBuilder).equal(adjudicationStatusPath,Status.CLEAR.name());
        doReturn(datePath).when(root).get("adjudicationCreatedAt");
        doReturn(predicate).when(criteriaBuilder).between(any(Path.class),any(LocalDate.class),any(LocalDate.class));
    }

    @Test
    void givenName_ReturnsSpecificationOfMatchingName(){

        Specification<Report> specificationOfMatchingName= ReportSpecification.hasNameLike("ra");
        Predicate actualPredicate=specificationOfMatchingName.toPredicate(root,query,criteriaBuilder);
        assertNotNull(actualPredicate,"Predicate should not be null");

        verify(root).join("candidate", JoinType.INNER);
        verify(criteriaBuilder).like(candidateNamePath, "%ra%");
    }

    @Test
    void givenNull_ReturnsNoSpecificationOfMatchingName(){

        Specification<Report> specificationOfMatchingName= ReportSpecification.hasNameLike(null);
        Predicate actualPredicate=specificationOfMatchingName.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no name is passed");
    }

    @Test
    void givenBlank_ReturnsNoSpecificationOfMatchingName(){

        Specification<Report> specificationOfMatchingName= ReportSpecification.hasNameLike("");
        Predicate actualPredicate=specificationOfMatchingName.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no name is passed");
    }

    @Test
    void givenAdjudication_ReturnsSpecificationThatMatchesWithGivenAdjudication(){

        Specification<Report> specificationOfAdjudication= ReportSpecification.hasAdjudication(Adjudication.PRE_ADVERSE_ACTION);
        Predicate actualPredicate=specificationOfAdjudication.toPredicate(root,query,criteriaBuilder);
        assertNotNull(actualPredicate,"Predicate should not be null");

        verify(root).get("adjudication");
        verify(criteriaBuilder).equal(adjudicationPath, Adjudication.PRE_ADVERSE_ACTION.name());
    }

    @Test
    void givenNull_ReturnsNoSpecificationThatMatchesWithAdjudication(){

        Specification<Report> specificationOfAdjudication= ReportSpecification.hasAdjudication(null);
        Predicate actualPredicate=specificationOfAdjudication.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no adjudication is passed");
    }

    @Test
    void givenAdjudicationStatus_ReturnsSpecificationThatMatchesWithGivenAdjudicationStatus(){

        Specification<Report> specificationOfAdjudicationStatus= ReportSpecification.hasAdjudicationStatus(Status.CLEAR);
        Predicate actualPredicate=specificationOfAdjudicationStatus.toPredicate(root,query,criteriaBuilder);
        assertNotNull(actualPredicate,"Predicate should not be null");

        verify(root).get("adjudicationStatus");
        verify(criteriaBuilder).equal(adjudicationStatusPath, Status.CLEAR.name());
    }

    @Test
    void givenNull_ReturnsNoSpecificationThatMatchesWithAdjudicationStatus(){

        Specification<Report> specificationOfAdjudicationStatus= ReportSpecification.hasAdjudicationStatus(null);
        Predicate actualPredicate=specificationOfAdjudicationStatus.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no adjudication status is passed");
    }

    @Test
    void givenDateRange_ReturnsSpecificationThatChecksTheGivenDateRange(){

        ExportDTO exportDTO=new ExportDTO(LocalDate.now(),LocalDate.now().plusDays(4));
        Specification<Report> specificationOfDateRange= ReportSpecification.hasDateRange(exportDTO);
        Predicate actualPredicate=specificationOfDateRange.toPredicate(root,query,criteriaBuilder);
        assertNotNull(actualPredicate,"Predicate should not be null");

        verify(root).get("adjudicationCreatedAt");
        verify(criteriaBuilder).between(any(Path.class),any(LocalDate.class),any(LocalDate.class));
    }

    @Test
    void givenNull_ReturnsNoSpecificationThatChecksTheDateRange(){

        Specification<Report> specificationOfDateRange= ReportSpecification.hasDateRange(null);
        Predicate actualPredicate=specificationOfDateRange.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no date range is passed");

    }
}

