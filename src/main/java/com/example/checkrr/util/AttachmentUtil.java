package com.example.checkrr.util;

import com.example.checkrr.exceptions.FileUploadFailedException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Setter
@Component
public class AttachmentUtil {

    @Value("${adverse-action.file.upload-dir}")
    private String uploadDir;


    public List<String> storeAttachmentAndGetURLs(MultipartFile[] files, Long candidateId) throws FileUploadFailedException {
        List<String> urlsList=new ArrayList<>();
        try{
            if(files!=null) {
                for (MultipartFile file : files) {
                    String filePath = uploadDir + candidateId + "__" + file.getOriginalFilename();
                    Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                    urlsList.add(filePath);
                }
                log.info("Files have been stored into the path {}",uploadDir);
            }
        } catch (IOException e) {
            throw new FileUploadFailedException("File upload failed. Try again");
        }
        return urlsList;
    }

}
