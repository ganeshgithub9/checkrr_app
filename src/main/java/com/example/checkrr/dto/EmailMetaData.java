package com.example.checkrr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailMetaData {
    @NotNull(message = "Sender id is required")
    private Long senderId;
    @NotNull(message = "Receiver id is required")
    private Long receiverId;
    @JsonProperty(value = "mailSubject")
    @NotBlank(message = "mail subject is required")
    private String subject;
    @NotBlank(message = "mail content is required")
    private String bodyInHtml;
}
