package com.jefferson.restful.h2.utils;

import com.jefferson.restful.h2.dao.entity.CurrencyMapping;
import com.jefferson.restful.h2.dao.repository.CurrencyMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConvertCurrencyNameUtils {

    private final CurrencyMappingRepository currencyMappingRepository;

    @Autowired
    public ConvertCurrencyNameUtils(CurrencyMappingRepository currencyMappingRepository) {
        this.currencyMappingRepository = currencyMappingRepository;
    }

    /**
     * 取得幣別中文名稱
     *
     * @param currencyCode
     * @return
     */
    public String getCurrencyChineseName(String currencyCode) {
        Optional<CurrencyMapping> existingMapping = currencyMappingRepository.findByCurrencyCode(currencyCode);
        return existingMapping.map(CurrencyMapping::getChineseName)
                .orElse(transToCurrencyChineseName(currencyCode));
    }

    /**
     * 選擇幣別中文名稱
     *
     * @param currencyCode
     * @return
     */
    public String transToCurrencyChineseName(String currencyCode) {
        switch (currencyCode) {
            case "USD":
                return "美元";
            case "GBP":
                return "英鎊";
            case "EUR":
                return "歐元";
            default:
                return currencyCode;
        }
    }
}
