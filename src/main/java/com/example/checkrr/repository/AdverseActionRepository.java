package com.example.checkrr.repository;

import com.example.checkrr.entity.AdverseAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdverseActionRepository extends JpaRepository<AdverseAction, Long> {
}