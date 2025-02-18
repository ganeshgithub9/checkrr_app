package com.example.checkrr.util;

import com.example.checkrr.exceptions.FileUploadFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.openMocks;


class AttachmentUtilTest {

    @InjectMocks
    AttachmentUtil attachmentUtil;

    ObjectMapper objectMapper=new ObjectMapper();

    MultipartFile[] files;

    @BeforeEach
    void setUp(){
        openMocks(this);
        attachmentUtil.setUploadDir("/home/ganeb/uploads/checkrr/");
        MultipartFile file1 = new MockMultipartFile("files", "test1.txt", "text/plain", "Dummy file content 1".getBytes(StandardCharsets.UTF_8));
        MultipartFile file2 = new MockMultipartFile("files", "test2.txt", "text/plain", "Dummy file content 2".getBytes(StandardCharsets.UTF_8));
        files=new MultipartFile[]{file1,file2};
    }

    @Test
    void givenFiles_WhenStoreAttachmentAndGetURLs_ThenStoresFilesAndReturnsURLs() throws FileUploadFailedException {
        try(MockedStatic<Files> mockedStatic= Mockito.mockStatic(Files.class)){

            List<String> expectedURLsList=List.of("/home/ganeb/uploads/checkrr/2__test1.txt","/home/ganeb/uploads/checkrr/2__test2.txt");

           mockedStatic.when(()-> Files.copy(any(InputStream.class),any(Path.class),any(CopyOption.class))).thenReturn(2L);


            List<String> urlsList=attachmentUtil.storeAttachmentAndGetURLs(files,2L);

            String expectedResult=objectMapper.writeValueAsString(expectedURLsList),actualResult=objectMapper.writeValueAsString(urlsList);
            assertEquals(expectedResult,actualResult);
            assertEquals(expectedURLsList.size(),urlsList.size());
            mockedStatic.verify(()->Files.copy(any(InputStream.class),any(Path.class),any(CopyOption.class)),Mockito.times(2));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenNoFiles_WhenStoreAttachmentAndGetURLs_ThenReturnsEmptyURLsList() throws FileUploadFailedException {
        files=null;
        List<String> urlsList=attachmentUtil.storeAttachmentAndGetURLs(files,2L);

        assertEquals(0,urlsList.size());
    }


    @Test
    void givenFiles_WhenStoreAttachmentAndGetURLs_ThenThrowsFileUploadFailedException() {
        try(MockedStatic<Files> mockedStatic= Mockito.mockStatic(Files.class)){

            mockedStatic.when(()-> Files.copy(any(InputStream.class),any(Path.class),any(CopyOption.class))).thenThrow(new IOException());

            String actualResult="";
            try {
                attachmentUtil.storeAttachmentAndGetURLs(files, 2L);
            }
            catch (FileUploadFailedException exception){
                actualResult=exception.getMessage();
            }

            String expectedResult="File upload failed. Try again";
            assertEquals(expectedResult,actualResult);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
