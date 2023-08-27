package pro.sky.animalshelter.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.exception.ReportNotFoundException;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.ReportCatShelter;
import pro.sky.animalshelter.model.ReportDogShelter;
import pro.sky.animalshelter.model.ReportStatus;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.ReportService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @Test
    public void testGetReportsByCatShelterAdopter() throws AdopterNotFoundException {
        // Arrange
        Integer adopterId = 1;
        List<ReportAnimalDTO> expectedReports = Collections.singletonList(new ReportAnimalDTO());

        when(reportService.getReportsCatShelterByAdopterAndStatus(eq(adopterId), any()))
                .thenReturn(expectedReports);

        // Act
        ResponseEntity<List<ReportAnimalDTO>> responseEntity =
                reportController.getReportsByCatShelterAdopter(adopterId, null);

        // Assert
        verify(reportService).getReportsCatShelterByAdopterAndStatus(adopterId, null);
        verifyNoMoreInteractions(reportService);

        assertEquals(expectedReports, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetReportsByCatShelterAdopter_AdopterNotFound() throws AdopterNotFoundException {
        // Arrange
        Integer adopterId = 1;

        when(reportService.getReportsCatShelterByAdopterAndStatus(eq(adopterId), any()))
                .thenThrow(new AdopterNotFoundException("Adopter not found"));

        // Act
        ResponseEntity<List<ReportAnimalDTO>> responseEntity =
                reportController.getReportsByCatShelterAdopter(adopterId, null);

        // Assert
        verify(reportService).getReportsCatShelterByAdopterAndStatus(adopterId, null);
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @Test
    public void testGetReportPhotoByDogShelterAdopter() throws Exception {
        // Мокируем данные отчета
        ReportDogShelter mockReport = new ReportDogShelter();
        mockReport.setPhotoFilePath("C:\\Users\\admin\\IdeaProjects\\Animal-shelter\\photo\\DOG_SHELTER_826249875_20230827_200759.jpg");
        mockReport.setPhotoMediaType("image/jpeg");
        mockReport.setPhotoFileSize(1024L); // Мокируем размер файла

        // Мокируем ответ и создаем ByteArrayOutputStream для ServletOutputStream
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
        when(mockResponse.getOutputStream()).thenReturn(servletOutputStream);

        // Мокируем поведение reportService
        when(reportService.getReportDogShelterById(anyInt())).thenReturn(mockReport);

        // Вызываем метод для тестирования
        reportController.getReportPhotoByDogShelterAdopter(1, mockResponse);

        // Проверяем взаимодействия
        verify(reportService).getReportDogShelterById(1);

        // Проверяем настройки ответа
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        verify(mockResponse).setContentType("image/jpeg");
        verify(mockResponse).setContentLengthLong(1024L);

        // Проверяем копирование фото
        try (InputStream is = new ByteArrayInputStream(outputStream.toByteArray())) {
            // При необходимости можно дополнительно проверить скопированный контент
        }
    }

    @Test
    public void testGetReportPhotoByCatShelterAdopter() throws Exception {
        // Мокируем данные отчета
        ReportCatShelter mockReport = new ReportCatShelter();
        mockReport.setPhotoFilePath("C:\\Users\\admin\\IdeaProjects\\Animal-shelter\\photo\\CAT_SHELTER_826249875_20230827_200724.jpg");
        mockReport.setPhotoMediaType("image/jpeg");
        mockReport.setPhotoFileSize(1024L); // Мокируем размер файла

        // Мокируем ответ и создаем ByteArrayOutputStream для ServletOutputStream
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
        when(mockResponse.getOutputStream()).thenReturn(servletOutputStream);

        // Мокируем поведение reportService
        when(reportService.getReportCatShelterById(anyInt())).thenReturn(mockReport);

        // Вызываем метод для тестирования
        reportController.getReportPhotoByCatShelterAdopter(1, mockResponse);

        // Проверяем взаимодействия
        verify(reportService).getReportCatShelterById(1);

        // Проверяем настройки ответа
        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        verify(mockResponse).setContentType("image/jpeg");
        verify(mockResponse).setContentLengthLong(1024L);

        // Проверяем копирование фото
        try (InputStream is = new ByteArrayInputStream(outputStream.toByteArray())) {
            // При необходимости можно дополнительно проверить скопированный контент
        }
    }

    @Test
    public void testGetReportsByDogShelterAdopter() throws ReportNotFoundException, AdopterNotFoundException {
        // Arrange
        Integer adopterId = 1;
        List<ReportAnimalDTO> expectedReports = Collections.singletonList(new ReportAnimalDTO());

        when(reportService.getReportsDogSheltersByAdopterAndStatus(eq(adopterId), any()))
                .thenReturn(expectedReports);

        // Act
        ResponseEntity<List<ReportAnimalDTO>> responseEntity =
                reportController.getReportsByDogShelterAdopter(adopterId, null);

        // Assert
        verify(reportService).getReportsDogSheltersByAdopterAndStatus(adopterId, null);
        verifyNoMoreInteractions(reportService);

        assertEquals(expectedReports, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetReportsByDogShelterAdopter_ReportNotFound() throws ReportNotFoundException, AdopterNotFoundException {
        // Arrange
        Integer adopterId = 1;

        when(reportService.getReportsDogSheltersByAdopterAndStatus(eq(adopterId), any()))
                .thenThrow(new ReportNotFoundException("Report not found"));

        // Act
        ResponseEntity<List<ReportAnimalDTO>> responseEntity =
                reportController.getReportsByDogShelterAdopter(adopterId, null);

        // Assert
        verify(reportService).getReportsDogSheltersByAdopterAndStatus(adopterId, null);
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetReportsByDogShelterAdopter_AdopterNotFound() throws ReportNotFoundException, AdopterNotFoundException {
        // Arrange
        Integer adopterId = 1;

        when(reportService.getReportsDogSheltersByAdopterAndStatus(eq(adopterId), any()))
                .thenThrow(new AdopterNotFoundException("Adopter not found"));

        // Act
        ResponseEntity<List<ReportAnimalDTO>> responseEntity =
                reportController.getReportsByDogShelterAdopter(adopterId, null);

        // Assert
        verify(reportService).getReportsDogSheltersByAdopterAndStatus(adopterId, null);
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @Test
    public void testProcessReportCatShelter() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;
        ReportAnimalDTO expectedReport = new ReportAnimalDTO();

        when(reportService.editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus)))
                .thenReturn(expectedReport);

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportCatShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(expectedReport, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testProcessReportCatShelter_AdopterNotFound() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;

        when(reportService.editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus)))
                .thenThrow(new AdopterNotFoundException("Adopter not found"));

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportCatShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testProcessReportCatShelter_ShelterNotFound() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;

        when(reportService.editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus)))
                .thenThrow(new ShelterNotFoundException("Shelter not found"));

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportCatShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testProcessReportCatShelter_ReportNotFound() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;

        when(reportService.editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus)))
                .thenThrow(new ReportNotFoundException("Report not found"));

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportCatShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.CAT_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @Test
    public void testProcessReportDogShelter() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;
        ReportAnimalDTO expectedReport = new ReportAnimalDTO();

        when(reportService.editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus)))
                .thenReturn(expectedReport);

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportDogShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(expectedReport, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testProcessReportDogShelter_AdopterNotFound() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;

        when(reportService.editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus)))
                .thenThrow(new AdopterNotFoundException("Adopter not found"));

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportDogShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testProcessReportDogShelter_ShelterNotFound() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;

        when(reportService.editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus)))
                .thenThrow(new ShelterNotFoundException("Shelter not found"));

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportDogShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testProcessReportDogShelter_ReportNotFound() throws AdopterNotFoundException, ShelterNotFoundException, ReportNotFoundException {
        // Arrange
        Integer reportId = 1;
        ReportStatus reportStatus = ReportStatus.REPORT_NEW;

        when(reportService.editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus)))
                .thenThrow(new ReportNotFoundException("Report not found"));

        // Act
        ResponseEntity<ReportAnimalDTO> responseEntity =
                reportController.processReportDogShelter(reportId, reportStatus);

        // Assert
        verify(reportService).editStatusReport(eq(ShelterType.DOG_SHELTER), eq(reportId), eq(reportStatus));
        verifyNoMoreInteractions(reportService);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}


