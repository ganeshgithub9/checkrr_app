package com.example.checkrr.specification;


import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.AdverseActionStatus;
import com.example.checkrr.specifications.AdverseActionSpecification;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class AdverseActionSpecificationTest {


    @Mock
    private Root<AdverseAction> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Join<AdverseAction, Candidate> candidateJoin;

    @Mock
    private Path<String> candidateNamePath;


    @Mock
    private Path<String> adverseActionStatusPath;


    @Mock
    Predicate predicate;


    @BeforeEach
    void setUp(){
        openMocks(this);

        doReturn(candidateJoin).when(root).join("candidate",JoinType.INNER);
        doReturn(candidateNamePath).when(candidateJoin).get("name");
        doReturn(candidateNamePath).when(criteriaBuilder).lower(candidateNamePath);
        doReturn(predicate).when(criteriaBuilder).like(candidateNamePath,"%ra%");
        doReturn(adverseActionStatusPath).when(root).get("status");
        doReturn(predicate).when(criteriaBuilder).equal(adverseActionStatusPath, AdverseActionStatus.CANCELED.name());
    }

    @Test
    void givenName_ReturnsSpecificationOfMatchingName(){

        Specification<AdverseAction> specificationOfMatchingName= AdverseActionSpecification.hasNameLike("ra");
        Predicate actualPredicate=specificationOfMatchingName.toPredicate(root,query,criteriaBuilder);
        assertNotNull(actualPredicate,"Predicate should not be null");

        verify(root).join("candidate", JoinType.INNER);
        verify(criteriaBuilder).like(candidateNamePath, "%ra%");
    }

    @Test
    void givenBlank_ReturnsNoSpecificationOfMatchingName(){

        Specification<AdverseAction> specificationOfMatchingName= AdverseActionSpecification.hasNameLike("");
        Predicate actualPredicate=specificationOfMatchingName.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no valid name is passed");

    }

    @Test
    void givenNull_ReturnsNoSpecificationOfMatchingName(){

        Specification<AdverseAction> specificationOfMatchingName= AdverseActionSpecification.hasNameLike(null);
        Predicate actualPredicate=specificationOfMatchingName.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no name is passed");

    }


    @Test
    void givenAdverseActionStatus_ReturnsSpecificationThatMatchesWithGivenAdverseActionStatus(){

        Specification<AdverseAction> specificationOfAdverseActionStatus= AdverseActionSpecification.hasStatus(AdverseActionStatus.CANCELED);
        Predicate actualPredicate=specificationOfAdverseActionStatus.toPredicate(root,query,criteriaBuilder);
        assertNotNull(actualPredicate,"Predicate should not be null");

        verify(root).get("status");
        verify(criteriaBuilder).equal(adverseActionStatusPath, AdverseActionStatus.CANCELED.name());
    }

    @Test
    void givenNull_ReturnsNoSpecificationThatMatchesWithAdverseActionStatus(){

        Specification<AdverseAction> specificationOfAdverseActionStatus= AdverseActionSpecification.hasStatus(null);
        Predicate actualPredicate=specificationOfAdverseActionStatus.toPredicate(root,query,criteriaBuilder);
        assertNull(actualPredicate,"Predicate should be null as no adverse action status is passed");

    }
}
