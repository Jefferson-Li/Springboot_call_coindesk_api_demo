package com.jefferson.restful.h2.service;

import com.jefferson.restful.h2.dao.entity.CurrencyMapping;
import com.jefferson.restful.h2.dao.repository.CurrencyMappingRepository;
import com.jefferson.restful.h2.dto.CurrencyInfoDto;
import com.jefferson.restful.h2.exception.DuplicateKeyException;
import com.jefferson.restful.h2.exception.ResourceNotFoundException;
import com.jefferson.restful.h2.model.CurrencyInfo;
import com.jefferson.restful.h2.utils.ConvertCurrencyNameUtils;
import com.jefferson.restful.h2.utils.DateTimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyMappingServiceTest {

    @Mock
    private CurrencyMappingRepository currencyMappingRepository;

    @Mock
    private DateTimeUtils dateTimeUtils;

    @Mock
    private ConvertCurrencyNameUtils convertCurrencyNameUtils;

    @InjectMocks
    private CurrencyMappingService currencyMappingService;

    private CurrencyMapping mockCurrencyMapping;
    private CurrencyInfoDto mockCurrencyInfoDto;

    @BeforeEach
    public void setUp() {
        // Create mock data
        mockCurrencyMapping = new CurrencyMapping();
        mockCurrencyMapping.setCurrencyCode("USD");
        mockCurrencyMapping.setChineseName("美元");
        mockCurrencyMapping.setRate("30.5");
        mockCurrencyMapping.setUpdatedISOTime(LocalDateTime.now());
        mockCurrencyMapping.setUpdatedTime(LocalDateTime.now());

        mockCurrencyInfoDto = new CurrencyInfoDto();
        CurrencyInfo currencyInfo = new CurrencyInfo();
        currencyInfo.setCurrencyCode("USD");
        currencyInfo.setChineseName("美元");
        currencyInfo.setRate("30.5");
        mockCurrencyInfoDto.setCurrencies(List.of(currencyInfo));
        mockCurrencyInfoDto.setUpdatedISOTime("2024/01/01 12:00:00");
    }

    @Test
    public void testFindAll_Success() {
        // Arrange
        List<CurrencyMapping> mockMappings = List.of(mockCurrencyMapping);
        when(currencyMappingRepository.findAll()).thenReturn(mockMappings);

        // Act
        List<CurrencyInfoDto> result = currencyMappingService.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(currencyMappingRepository, times(1)).findAll();
    }

    @Test
    public void testFindByCurrencyCode_Success() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode("USD"))
                .thenReturn(Optional.of(mockCurrencyMapping));

        // Act
        CurrencyInfoDto result = currencyMappingService.findByCurrencyCode("USD");

        // Assert
        assertNotNull(result);
        assertEquals("USD", result.getCurrencies().get(0).getCurrencyCode());
        verify(currencyMappingRepository, times(1)).findByCurrencyCode("USD");
    }

    @Test
    public void testFindByCurrencyCode_NotFound() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode("XYZ"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            currencyMappingService.findByCurrencyCode("XYZ");
        });
    }

    @Test
    public void testCreate_Success() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode(any())).thenReturn(Optional.empty());
        when(currencyMappingRepository.saveAll(any())).thenReturn(List.of(mockCurrencyMapping));
        when(convertCurrencyNameUtils.transToCurrencyChineseName(any())).thenReturn("美元");
        when(dateTimeUtils.parseISOTime(any())).thenReturn(LocalDateTime.now());

        // Act
        CurrencyInfoDto result = currencyMappingService.create(mockCurrencyInfoDto);

        // Assert
        assertNotNull(result);
        verify(currencyMappingRepository, times(1)).saveAll(any());
    }

    @Test
    public void testCreate_DuplicateCurrency() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode("USD"))
                .thenReturn(Optional.of(mockCurrencyMapping));

        // Act & Assert
        assertThrows(DuplicateKeyException.class, () -> {
            currencyMappingService.create(mockCurrencyInfoDto);
        });
    }

    @Test
    public void testCreate_EmptyCurrenciesList() {
        // Arrange
        CurrencyInfoDto emptyDto = new CurrencyInfoDto();
        emptyDto.setCurrencies(new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            currencyMappingService.create(emptyDto);
        });
    }

    @Test
    public void testUpdate_Success() {
        // Arrange
        CurrencyMapping existingMapping = new CurrencyMapping();
        existingMapping.setCurrencyCode("USD");

        when(currencyMappingRepository.findByCurrencyCode("USD"))
                .thenReturn(Optional.of(existingMapping));
        when(currencyMappingRepository.save(any())).thenReturn(existingMapping);

        // Act
        CurrencyInfoDto result = currencyMappingService.update("USD", mockCurrencyInfoDto);

        // Assert
        assertNotNull(result);
        verify(currencyMappingRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate_NotFound() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode("XYZ"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            currencyMappingService.update("XYZ", mockCurrencyInfoDto);
        });
    }

    @Test
    public void testDelete_Success() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode("USD"))
                .thenReturn(Optional.of(mockCurrencyMapping));

        // Act
        currencyMappingService.delete("USD");

        // Assert
        verify(currencyMappingRepository, times(1)).delete(any());
    }

    @Test
    public void testDelete_NotFound() {
        // Arrange
        when(currencyMappingRepository.findByCurrencyCode("XYZ"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            currencyMappingService.delete("XYZ");
        });
    }
}