package com.example.checkrr.repository;

import com.example.checkrr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u.email from User u where u.id=:id")
    String findEmailById(@Param("id") Long senderId);
}