package pro.sky.animalshelter.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.FileService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @Test
    public void testUploadDataFileDogShelter_Success() throws IOException {
        // Создаем заглушку для MultipartFile
        MultipartFile multipartFile = mock(MultipartFile.class);


        // Вызываем метод контроллера
        ResponseEntity<Void> response = fileController.uploadDataFileDogShelter(
                multipartFile, ShelterType.DOG_SHELTER, "Happy Dog");

        // Проверяем, что ответ имеет статус 200 OK
        assert response.getStatusCode() == HttpStatus.OK;

        // Проверяем, что метод fileService.saveDirectoryToRepository был вызван один раз
        verify(fileService, times(1)).saveDirectoryToRepository(
                eq(ShelterType.DOG_SHELTER), any(MultipartFile.class));
    }

    @Test
    public void testUploadDataFileDogShelter_Failure() throws IOException {
        // Создаем заглушку для MultipartFile
        MultipartFile multipartFile = mock(MultipartFile.class);

        // Мокируем вызовы методов в fileService и предоставляем исключение
        when(fileService.saveDirectoryToRepository(any(), any())).thenThrow(new IOException());

        // Вызываем метод контроллера
        ResponseEntity<Void> response = fileController.uploadDataFileDogShelter(
                multipartFile, ShelterType.DOG_SHELTER, "Happy Dog");

        // Проверяем, что ответ имеет статус 500 Internal Server Error
        assert response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;

        // Проверяем, что метод fileService.saveDirectoryToRepository был вызван один раз
        verify(fileService, times(1)).saveDirectoryToRepository(
                eq(ShelterType.DOG_SHELTER), any(MultipartFile.class));
    }

    @Test
    public void testDownloadFileById() throws IOException {
        // Arrange
        ShelterType shelterType = ShelterType.DOG_SHELTER;

        // Создаем временный файл для теста
        File tempFile = createTempFile();

        when(fileService.getDataFile(shelterType)).thenReturn(tempFile);

        // Act
        ResponseEntity<InputStreamResource> responseEntity = fileController.downloadFileById(shelterType);

        // Assert
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(MediaType.IMAGE_PNG, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals(tempFile.length(), responseEntity.getHeaders().getContentLength());
        Assertions.assertEquals("attachment; filename =\"DogShelter.pnj\"", responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        // Проверяем, что fileService.getDataFile был вызван с правильным аргументом
        verify(fileService, times(1)).getDataFile(shelterType);
    }

    private File createTempFile() throws IOException {
        // Создаем временный файл для теста
        File tempFile = File.createTempFile("test", ".png");
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    public void testDownloadFileById_FileNotExists() throws FileNotFoundException {
        // Заглушка для File
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(false);

        // Заглушка для FileService
        when(fileService.getDataFile(any())).thenReturn(mockFile);

        // Вызываем метод контроллера
        ResponseEntity<InputStreamResource> response = fileController.downloadFileById(ShelterType.DOG_SHELTER);

        // Проверяем, что ответ имеет статус 204 No Content
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}

