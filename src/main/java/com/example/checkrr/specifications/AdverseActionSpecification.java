package com.example.checkrr.specifications;

import com.example.checkrr.entity.AdverseAction;
import com.example.checkrr.entity.Candidate;
import com.example.checkrr.enums.AdverseActionStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class AdverseActionSpecification {
    private AdverseActionSpecification(){}
    public static Specification<AdverseAction> hasNameLike(String name){
        return (root,query,cb)->{
            if (name == null || name.isEmpty()) return null;
            Join<AdverseAction, Candidate> candidateJoin = root.join("candidate", JoinType.INNER);
            return cb.like(cb.lower(candidateJoin.get("name")), "%"+name.toLowerCase()+"%");
        };
    }

    public static Specification<AdverseAction> hasStatus(AdverseActionStatus status){
        return (root,query,criteriaBuilder)->{
            if (status == null) return null;
            return criteriaBuilder.equal(root.get("status"),status.name() );
        };
    }
}
