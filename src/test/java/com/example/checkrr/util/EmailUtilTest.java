package com.example.checkrr.util;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class EmailUtilTest {

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailUtil emailUtil;

    MimeMessage message;
    MultipartFile[] files;

    @BeforeEach
    void setUp(){
        openMocks(this);
        emailUtil.senderMailId="aaab@busam.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        Session session = Session.getInstance(props, null);

        message = new MimeMessage(session);
        MockMultipartFile file1 = new MockMultipartFile("files", "test1.txt", "text/plain", "Dummy file content 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("files", "test2.txt", "text/plain", "Dummy file content 2".getBytes(StandardCharsets.UTF_8));
        files=new MultipartFile[]{file1,file2};
    }

    @Test
    void givenMailDetails_WhenSendMailWithAttachments_ThenSendsMailToUserAndCandidate() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(message);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailUtil.sendMailWithAttachments("abc@abc.com","xyz@abc.com","Test","<div>Hello, This is a test message<div/>",files);

        verify(mailSender,times(1)).createMimeMessage();
        verify(mailSender,times(1)).send(message);
    }

    @Test
    void givenMailDetailsWithNoFiles_WhenSendMailWithAttachments_ThenSendMailsToUserAndCandidateWithNoAttachments() throws MessagingException {
        files=null;
        when(mailSender.createMimeMessage()).thenReturn(message);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailUtil.sendMailWithAttachments("abc@abc.com","xyz@abc.com","Test","<div>Hello, This is a test message<div/>",files);

        verify(mailSender,times(1)).createMimeMessage();
        verify(mailSender,times(1)).send(message);
    }
}
