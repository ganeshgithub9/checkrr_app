package com.example.checkrr.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
@Slf4j
@Component
public class EmailUtil {



    JavaMailSender mailSender;
    String senderMailId;

    EmailUtil(@Autowired JavaMailSender mailSender, @Value("${mail.sender-id}") String senderMailId){
        this.mailSender=mailSender;
        this.senderMailId=senderMailId;
    }

     public void sendMailWithAttachments(String userMailId, String candidateMailId, String subject, String bodyInHtml, MultipartFile[] attachments) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(senderMailId);
        helper.setTo(new String[]{userMailId,candidateMailId});
        helper.setSubject(subject);
        helper.setText(bodyInHtml, true); // true => HTML content allowed

        // Attach files
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }

        mailSender.send(message);
        log.info("Email has been sent from {} to {} and {}",senderMailId,userMailId,candidateMailId);
    }
}