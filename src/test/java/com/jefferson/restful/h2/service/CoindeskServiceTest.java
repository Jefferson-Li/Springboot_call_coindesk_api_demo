package com.jefferson.restful.h2.service;

import com.jefferson.restful.h2.dao.entity.CurrencyMapping;
import com.jefferson.restful.h2.dao.repository.CurrencyMappingRepository;
import com.jefferson.restful.h2.dto.BitcoinPriceResponse;
import com.jefferson.restful.h2.dto.CurrencyInfoDto;
import com.jefferson.restful.h2.model.BPI;
import com.jefferson.restful.h2.model.Currency;
import com.jefferson.restful.h2.model.CurrencyInfo;
import com.jefferson.restful.h2.model.Time;
import com.jefferson.restful.h2.utils.ConvertCurrencyNameUtils;
import com.jefferson.restful.h2.utils.DateTimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CoindeskServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyMappingRepository currencyMappingRepository;

    @Mock
    private DateTimeUtils dateTimeUtils;

    @Mock
    private ConvertCurrencyNameUtils convertCurrencyNameUtils;

    @InjectMocks
    private CoindeskService coindeskService;

    private BitcoinPriceResponse mockResponse;

    @BeforeEach
    public void setUp() {
        // 創建模擬的 Coindesk API 回應
        mockResponse = new BitcoinPriceResponse();

        Time time = new Time();
        time.setUpdatedISO("2024-01-01T12:00:00+00:00");
        mockResponse.setTime(time);

        BPI bpi = new BPI();

        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setDescription("United States Dollar");
        usd.setRate("50000.00");
        bpi.setUsd(usd);

        Currency eur = new Currency();
        eur.setCode("EUR");
        eur.setDescription("Euro");
        eur.setRate("45000.00");
        bpi.setEur(eur);

        mockResponse.setBpi(bpi);
    }

    @Test
    public void testGetCoindeskApiRes() {
        // Arrange
        when(restTemplate.getForObject(
                eq("https://api.coindesk.com/v1/bpi/currentprice.json"),
                eq(BitcoinPriceResponse.class)
        )).thenReturn(mockResponse);

        // Act
        BitcoinPriceResponse result = coindeskService.getCoindeskApiRes();

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).getForObject(
                eq("https://api.coindesk.com/v1/bpi/currentprice.json"),
                eq(BitcoinPriceResponse.class)
        );
    }

    @Test
    void getCoindeskApiRes_ShouldReturnBitcoinPriceResponse() {
        // Arrange
        BitcoinPriceResponse mockResponse = createMockBitcoinPriceResponse();
        when(restTemplate.getForObject(anyString(), eq(BitcoinPriceResponse.class)))
                .thenReturn(mockResponse);

        // Act
        BitcoinPriceResponse response = coindeskService.getCoindeskApiRes();

        // Assert
        assertNotNull(response);
        verify(restTemplate).getForObject(eq("https://api.coindesk.com/v1/bpi/currentprice.json"),
                eq(BitcoinPriceResponse.class));
    }

    @Test
    void getCoinDeskCurrentPriceTransToNewApi_ShouldProcessAndReturnCurrencyInfoDto() {
        // Arrange
        BitcoinPriceResponse mockResponse = createMockBitcoinPriceResponse();
        when(restTemplate.getForObject(anyString(), eq(BitcoinPriceResponse.class)))
                .thenReturn(mockResponse);

        // Mock date formatting
        String formattedTime = "2023-01-01T12:00:00Z";
        when(dateTimeUtils.formatUpdateTime(anyString())).thenReturn(formattedTime);

        // Mock currency name conversion
        when(convertCurrencyNameUtils.getCurrencyChineseName(anyString()))
                .thenReturn("美元");

        // Mock repository behavior
        when(currencyMappingRepository.findByCurrencyCode(anyString()))
                .thenReturn(Optional.of(createMockCurrencyMapping()));

        // Act
        CurrencyInfoDto result = coindeskService.getCoinDeskCurrentPriceTransToNewApi();

        // Assert
        assertNotNull(result);
        assertEquals(formattedTime, result.getUpdatedISOTime());

        List<CurrencyInfo> currencies = result.getCurrencies();
        assertNotNull(currencies);
        assertFalse(currencies.isEmpty());

    }

    private BitcoinPriceResponse createMockBitcoinPriceResponse() {
        BitcoinPriceResponse response = new BitcoinPriceResponse();

        // Create mock Time
        Time time = new Time();
        time.setUpdatedISO("2023-01-01T12:00:00Z");
        response.setTime(time);

        // Create mock BPI
        BPI bpi = new BPI();

        // USD Currency
        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setRate("50000.00");
        usd.setDescription("United States Dollar");
        bpi.setUsd(usd);

        // GBP Currency
        Currency gbp = new Currency();
        gbp.setCode("GBP");
        gbp.setRate("40000.00");
        gbp.setDescription("British Pound Sterling");
        bpi.setGbp(gbp);

        // EUR Currency
        Currency eur = new Currency();
        eur.setCode("EUR");
        eur.setRate("45000.00");
        eur.setDescription("Euro");
        bpi.setEur(eur);

        response.setBpi(bpi);

        return response;
    }

    private CurrencyMapping createMockCurrencyMapping() {
        CurrencyMapping mapping = new CurrencyMapping();
        mapping.setCurrencyCode("USD");
        mapping.setChineseName("美元");
        mapping.setRate("50000.00");
        mapping.setUpdatedTime(OffsetDateTime.now().toLocalDateTime());
        return mapping;
    }
}