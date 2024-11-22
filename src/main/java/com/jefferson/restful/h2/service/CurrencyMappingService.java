package com.jefferson.restful.h2.service;

import com.jefferson.restful.h2.dao.entity.CurrencyMapping;
import com.jefferson.restful.h2.dao.repository.CurrencyMappingRepository;
import com.jefferson.restful.h2.dto.CurrencyInfoDto;
import com.jefferson.restful.h2.exception.DuplicateKeyException;
import com.jefferson.restful.h2.exception.ResourceNotFoundException;
import com.jefferson.restful.h2.model.CurrencyInfo;
import com.jefferson.restful.h2.utils.ConvertCurrencyNameUtils;
import com.jefferson.restful.h2.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyMappingService {
    private final CurrencyMappingRepository currencyMappingRepository;
    private final DateTimeUtils dateTimeUtils;
    private final ConvertCurrencyNameUtils convertCurrencyNameUtils;

    @Autowired
    public CurrencyMappingService(CurrencyMappingRepository currencyMappingRepository, DateTimeUtils dateTimeUtils, ConvertCurrencyNameUtils convertCurrencyNameUtils) {
        this.currencyMappingRepository = currencyMappingRepository;
        this.dateTimeUtils = dateTimeUtils;
        this.convertCurrencyNameUtils = convertCurrencyNameUtils;
    }

    /**
     * 查詢所有幣別
     *
     * @return
     */
    public List<CurrencyInfoDto> findAll() {
        return convertToCurrencyInfoDtos(currencyMappingRepository.findAll());
    }

    /**
     * 根據代碼查詢幣別
     *
     * @param currencyCode
     * @return
     */
    public CurrencyInfoDto findByCurrencyCode(String currencyCode) {
        return currencyMappingRepository.findByCurrencyCode(currencyCode)
                .map(this::convertToCurrencyInfoDto)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with code: " + currencyCode));
    }

    /**
     * 新增幣別
     *
     * @param dto
     * @return
     */
    public CurrencyInfoDto create(CurrencyInfoDto dto) {
        // 安全檢查 currencies 是否為空
        List<CurrencyInfo> currencies = Optional.ofNullable(dto.getCurrencies())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Currencies list cannot be empty"));

        // 取第一個 currency
        CurrencyInfo currencyInfo = currencies.get(0);

        // 檢查是否已存在
        currencyMappingRepository.findByCurrencyCode(currencyInfo.getCurrencyCode())
                .ifPresent(existing -> {
                    throw new DuplicateKeyException("Currency already exists with code: " + existing.getCurrencyCode());
                });

        // 轉換並儲存
        List<CurrencyMapping> mappings = convertToCurrencyMappings(dto);
        List<CurrencyMapping> savedMappings = currencyMappingRepository.saveAll(mappings);

        return convertToCurrencyInfoDto(savedMappings);
    }

    /**
     * 更新幣別
     *
     * @param currencyCode
     * @param dto
     * @return
     */
    public CurrencyInfoDto update(String currencyCode, CurrencyInfoDto dto) {
        // 安全檢查 currencies 是否為空
        List<CurrencyInfo> currencies = Optional.ofNullable(dto.getCurrencies())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Currencies list cannot be empty"));

        // 取第一個 currency
        CurrencyInfo currencyInfo = currencies.get(0);

        CurrencyMapping existingCurrency = currencyMappingRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with code: " + currencyCode));

        existingCurrency.setChineseName(currencyInfo.getChineseName());
        existingCurrency.setRate(currencyInfo.getRate());
        existingCurrency.setUpdatedTime(LocalDateTime.now());

        return convertToCurrencyInfoDto(currencyMappingRepository.save(existingCurrency));
    }

    /**
     * 刪除幣別
     *
     * @param currencyCode
     */
    public void delete(String currencyCode) {
        CurrencyMapping currency = currencyMappingRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with code: " + currencyCode));
        currencyMappingRepository.delete(currency);
    }

    /**
     * Entity 轉 DTO
     *
     * @param currencyMapping
     * @return
     */
    public CurrencyInfoDto convertToCurrencyInfoDto(CurrencyMapping currencyMapping) {
        return Optional.ofNullable(currencyMapping)
                .map(mapping -> {
                    CurrencyInfoDto dto = new CurrencyInfoDto();
                    dto.setUpdatedISOTime(
                            Optional.ofNullable(mapping.getUpdatedISOTime())
                                    .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                    .orElse(null)
                    );

                    CurrencyInfo currencyInfo = new CurrencyInfo();
                    currencyInfo.setCurrencyCode(mapping.getCurrencyCode());
                    currencyInfo.setChineseName(mapping.getChineseName());
                    currencyInfo.setRate(mapping.getRate());
                    currencyInfo.setUpdatedTime(
                            Optional.ofNullable(mapping.getUpdatedTime())
                                    .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                    .orElse(null)
                    );

                    dto.setCurrencies(Collections.singletonList(currencyInfo));
                    return dto;
                })
                .orElse(new CurrencyInfoDto());
    }

    /**
     * List Entity 轉 DTO
     *
     * @param mappings
     * @return
     */
    public CurrencyInfoDto convertToCurrencyInfoDto(List<CurrencyMapping> mappings) {
        return Optional.ofNullable(mappings)
                .filter(list -> !list.isEmpty())
                .map(list -> {
                    CurrencyInfoDto dto = new CurrencyInfoDto();
                    dto.setUpdatedISOTime(
                            Optional.ofNullable(list.get(0).getUpdatedISOTime())
                                    .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                    .orElse(null)
                    );

                    List<CurrencyInfo> currencies = list.stream()
                            .map(mapping -> {
                                CurrencyInfo info = new CurrencyInfo();
                                info.setCurrencyCode(mapping.getCurrencyCode());
                                info.setChineseName(mapping.getChineseName());
                                info.setRate(mapping.getRate());
                                info.setUpdatedTime(Optional.ofNullable(mapping.getUpdatedTime())
                                        .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                        .orElse(null));
                                return info;
                            })
                            .collect(Collectors.toList());

                    dto.setCurrencies(currencies);
                    return dto;
                })
                .orElse(new CurrencyInfoDto());
    }

    /**
     * List Entity 轉 List DTO
     *
     * @param mappings
     * @return
     */
    public List<CurrencyInfoDto> convertToCurrencyInfoDtos(List<CurrencyMapping> mappings) {
        return Optional.ofNullable(mappings)
                .filter(list -> !list.isEmpty())
                .map(list -> {
                    Map<LocalDateTime, List<CurrencyMapping>> groupedByUpdatedTime = list.stream()
                            .collect(Collectors.groupingBy(
                                    CurrencyMapping::getUpdatedISOTime,
                                    Collectors.toList()
                            ));

                    return groupedByUpdatedTime.entrySet().stream()
                            .map(entry -> {
                                CurrencyInfoDto dto = new CurrencyInfoDto();
                                dto.setUpdatedISOTime(
                                        Optional.ofNullable(entry.getKey())
                                                .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                                .orElse(null)
                                );

                                List<CurrencyInfo> currencies = entry.getValue().stream()
                                        .map(mapping -> {
                                            CurrencyInfo info = new CurrencyInfo();
                                            info.setCurrencyCode(mapping.getCurrencyCode());
                                            info.setChineseName(mapping.getChineseName());
                                            info.setRate(mapping.getRate());
                                            info.setUpdatedTime(Optional.ofNullable(mapping.getUpdatedTime())
                                                    .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                                                    .orElse(null));
                                            return info;
                                        })
                                        .collect(Collectors.toList());

                                dto.setCurrencies(currencies);
                                return dto;
                            })
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    /**
     * DTO 轉 List Entity
     *
     * @param infoDto
     * @return
     */
    public List<CurrencyMapping> convertToCurrencyMappings(CurrencyInfoDto infoDto) {
        return Optional.ofNullable(infoDto.getCurrencies())
                .map(currencies -> currencies.stream()
                        .map(currencyInfo -> {
                            CurrencyMapping mapping = new CurrencyMapping();
                            mapping.setCurrencyCode(currencyInfo.getCurrencyCode());
                            mapping.setChineseName(convertCurrencyNameUtils.getCurrencyChineseName(currencyInfo.getCurrencyCode()));
                            mapping.setChineseName(currencyInfo.getChineseName());
                            mapping.setRate(currencyInfo.getRate());
                            mapping.setUpdatedISOTime(dateTimeUtils.parseISOTime(infoDto.getUpdatedISOTime()));
                            mapping.setUpdatedTime(LocalDateTime.now());
                            return mapping;
                        })
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

}