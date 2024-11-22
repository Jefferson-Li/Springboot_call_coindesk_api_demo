package com.jefferson.restful.h2.service;

import com.jefferson.restful.h2.dao.entity.CurrencyMapping;
import com.jefferson.restful.h2.dao.repository.CurrencyMappingRepository;
import com.jefferson.restful.h2.dto.BitcoinPriceResponse;
import com.jefferson.restful.h2.dto.CurrencyInfoDto;
import com.jefferson.restful.h2.model.BPI;
import com.jefferson.restful.h2.model.Currency;
import com.jefferson.restful.h2.model.CurrencyInfo;
import com.jefferson.restful.h2.utils.ConvertCurrencyNameUtils;
import com.jefferson.restful.h2.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoindeskService {
    private static final String COINDESK_API_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private final RestTemplate restTemplate;
    private final CurrencyMappingRepository currencyMappingRepository;
    private final DateTimeUtils dateTimeUtils;
    private final ConvertCurrencyNameUtils convertCurrencyNameUtils;

    @Autowired
    public CoindeskService(RestTemplate restTemplate, CurrencyMappingRepository currencyMappingRepository,
                           DateTimeUtils dateTimeUtils, ConvertCurrencyNameUtils convertCurrencyNameUtils) {
        this.restTemplate = restTemplate;
        this.currencyMappingRepository = currencyMappingRepository;
        this.dateTimeUtils = dateTimeUtils;
        this.convertCurrencyNameUtils = convertCurrencyNameUtils;
    }

    /**
     * 呼叫 coinDesk 的API。
     *
     * @return
     */
    public BitcoinPriceResponse getCoindeskApiRes() {
        // 1. 呼叫Coindesk API
        BitcoinPriceResponse coindeskResponse = restTemplate.getForObject(
                COINDESK_API_URL,
                BitcoinPriceResponse.class
        );
        return coindeskResponse;
    }

    /**
     * 呼叫 coinDesk 的API，並進行資料轉換，組成新API。
     *
     * @return
     */
    public CurrencyInfoDto getCoinDeskCurrentPriceTransToNewApi() {
        // 1. 呼叫 Coindesk API
        BitcoinPriceResponse coindeskResponse = restTemplate.getForObject(COINDESK_API_URL, BitcoinPriceResponse.class);

        // 2. 轉換資料
        CurrencyInfoDto response = new CurrencyInfoDto();
        String formattedUpdatedISOTime = dateTimeUtils.formatUpdateTime(coindeskResponse.getTime().getUpdatedISO());
        response.setUpdatedISOTime(formattedUpdatedISOTime);

        // 3. 處理幣別資訊
        BPI bpi = coindeskResponse.getBpi();
        List<CurrencyMapping> currencyMappings = processCurrencyMappings(bpi, formattedUpdatedISOTime);
        currencyMappingRepository.saveAll(currencyMappings);

        // 4. 準備回傳資料
        response.setCurrencies(processCurrencyInfoList(bpi));
        return response;
    }

    /**
     * 處理幣別資訊
     *
     * @param bpi
     * @param updatedTime
     * @return
     */
    private List<CurrencyMapping> processCurrencyMappings(BPI bpi, String updatedTime) {
        List<CurrencyMapping> mappings = new ArrayList<>();
        processCurrencyMapping(bpi.getUsd(), updatedTime, mappings);
        processCurrencyMapping(bpi.getGbp(), updatedTime, mappings);
        processCurrencyMapping(bpi.getEur(), updatedTime, mappings);
        return mappings;
    }

    /**
     * 處理幣別資訊
     *
     * @param currency
     * @param updatedTime
     * @param mappings
     */
    private void processCurrencyMapping(Currency currency, String updatedTime, List<CurrencyMapping> mappings) {
        if (currency != null) {
            mappings.add(createCurrencyMapping(currency, updatedTime));
        }
    }

    /**
     * 準備回傳資料
     *
     * @param bpi
     * @return
     */
    private List<CurrencyInfo> processCurrencyInfoList(BPI bpi) {
        List<CurrencyInfo> currencyInfoList = new ArrayList<>();
        processCurrencyInfo(bpi.getUsd(), currencyInfoList);
        processCurrencyInfo(bpi.getGbp(), currencyInfoList);
        processCurrencyInfo(bpi.getEur(), currencyInfoList);
        return currencyInfoList;
    }

    /**
     * 準備回傳資料
     *
     * @param currency
     * @param currencyInfoList
     */
    private void processCurrencyInfo(Currency currency, List<CurrencyInfo> currencyInfoList) {
        if (currency != null) {
            addCurrencyInfo(currencyInfoList, currency);
        }
    }

    /**
     * 處理幣別資訊
     *
     * @param currencyInfoList
     * @param currency
     */
    private void addCurrencyInfo(List<CurrencyInfo> currencyInfoList, Currency currency) {

        if (currency == null) {
            return;  // 如果傳入的 currency 為 null，直接返回
        }

        CurrencyInfo info = new CurrencyInfo();
        info.setCurrencyCode(currency.getCode());

        // 從資料庫獲取中文名稱
        try {
            Optional<CurrencyMapping> mapping = currencyMappingRepository.findByCurrencyCode(currency.getCode());
            if (mapping.isPresent()) {
                info.setChineseName(mapping.get().getChineseName());
                info.setRate(currency.getRate());
                info.setUpdatedTime(Optional.ofNullable(mapping.get().getUpdatedTime())
                        .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                        .orElse(null));
            } else {
                // 如果找不到對應的中文名稱，使用英文描述
                info.setChineseName(currency.getDescription());
            }
        } catch (Exception e) {
            // 如果發生任何異常，使用英文描述
            info.setChineseName(currency.getDescription());
        }

        currencyInfoList.add(info);
    }

    /**
     * 轉成 entity
     *
     * @param currency
     * @return
     */
    private CurrencyMapping createCurrencyMapping(Currency currency, String formattedUpdatedISOTime) {
        CurrencyMapping mapping = new CurrencyMapping();
        mapping.setCurrencyCode(currency.getCode());
        mapping.setChineseName(currency.getDescription());
        mapping.setChineseName(convertCurrencyNameUtils.getCurrencyChineseName(currency.getCode()));
        mapping.setRate(currency.getRate());
        mapping.setUpdatedISOTime(dateTimeUtils.parseISOTime(formattedUpdatedISOTime));

        // 轉換時間格式
        mapping.setUpdatedTime(OffsetDateTime.now().toLocalDateTime());
        return mapping;
    }

}
