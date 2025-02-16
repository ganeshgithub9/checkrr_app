package com.example.checkrr.service;

import com.example.checkrr.entity.User;
import com.example.checkrr.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomUserService userService;

    User user;

    @BeforeEach
    void setUp(){
        openMocks(this);
        user=new User(3L,"jack","xyz@abc.com","@#45~@qa",null);
    }

    @Test
    void givenUserId_WhenGetUserReferenceById_ThenReturnsUser(){
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);

        User actualUser=userService.getUserReferenceById(3L);

        assertEquals(user,actualUser);
        assertEquals(user.getName(),actualUser.getName());
        assertEquals(user.getEmail(),actualUser.getEmail());
    }

}
