package com.example.checkrr.service;

import com.example.checkrr.entity.User;
import com.example.checkrr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserService{


    UserRepository repository;

    CustomUserService(@Autowired UserRepository repository){
        this.repository=repository;
    }
    @Override
    public User getUserReferenceById(Long userId) {
        return repository.getReferenceById(userId);
    }

    @Override
    public String getEmailById(Long senderId) {
        return repository.findEmailById(senderId);
    }
}
