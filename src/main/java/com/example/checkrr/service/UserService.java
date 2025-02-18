package com.example.checkrr.service;

import com.example.checkrr.entity.User;
import jakarta.validation.constraints.NotNull;

public interface UserService {
    User getUserReferenceById(Long userId);

    String getEmailById(@NotNull(message = "Sender id is required") Long senderId);
}
