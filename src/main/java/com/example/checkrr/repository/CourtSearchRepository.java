package com.example.checkrr.repository;

import com.example.checkrr.entity.CourtSearch;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtSearchRepository extends JpaRepository<CourtSearch, Long> {
}