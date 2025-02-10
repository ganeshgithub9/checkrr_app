package com.example.checkrr.specifications;

import com.example.checkrr.dto.ExportDTO;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.entity.Report;
import com.example.checkrr.enums.Adjudication;
import com.example.checkrr.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ReportSpecification {
    private ReportSpecification(){}
    public static Specification<Report> hasNameLike(String name){
        return (root, query, criteriaBuilder) ->{
            if(name==null || name.isEmpty())
                return null;
            Join<Candidate, Report> candidateJoin=root.join("candidate", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(candidateJoin.get("name")),"%"+name.toLowerCase()+"%");
        };
    }

    public static Specification<Report> hasAdjudication(Adjudication adjudication){
        return (root, query, criteriaBuilder) ->{
            if(adjudication==null)
                return null;
            return criteriaBuilder.equal(root.get("adjudication"), adjudication.name());
        };
    }

    public static Specification<Report> hasAdjudicationStatus(Status adjudicationStatus){
        return (root, query, criteriaBuilder) ->{
            if(adjudicationStatus==null)
                return null;
            return criteriaBuilder.equal(root.get("adjudicationStatus"), adjudicationStatus.name());
        };
    }

    public static Specification<Report> hasDateRange(ExportDTO exportDTO){
        return (root, query, criteriaBuilder) ->{
            if(exportDTO==null)
                return null;
            return criteriaBuilder.between(root.get("adjudicationCreatedAt"),exportDTO.getFrom(),exportDTO.getTo());
        };
    }
}
