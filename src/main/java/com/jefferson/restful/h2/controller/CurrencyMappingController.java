package com.jefferson.restful.h2.controller;

import com.jefferson.restful.h2.dto.CurrencyInfoDto;
import com.jefferson.restful.h2.service.CurrencyMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyMappingController {

    private final CurrencyMappingService currencyMappingService;

    @Autowired
    public CurrencyMappingController(CurrencyMappingService currencyMappingService) {
        this.currencyMappingService = currencyMappingService;
    }

    /**
     * 查詢所有幣別
     *
     * @return 幣別表所有資訊
     */
    @GetMapping
    public ResponseEntity<List<CurrencyInfoDto>> getAllCurrencies() {

        return ResponseEntity.ok(currencyMappingService.findAll());
    }

    /**
     * 查詢特定幣別
     *
     * @param currencyCode
     * @return
     */
    @GetMapping("/{currencyCode}")
    public ResponseEntity<CurrencyInfoDto> getCurrency(@PathVariable String currencyCode) {
        return ResponseEntity.ok(currencyMappingService.findByCurrencyCode(currencyCode));
    }

    /**
     * 新增幣別
     *
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseEntity<CurrencyInfoDto> createCurrency(@RequestBody CurrencyInfoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(currencyMappingService.create(dto));
    }

    /**
     * 更新幣別
     *
     * @param currencyCode
     * @param dto
     * @return
     */
    @PutMapping("/{currencyCode}")
    public ResponseEntity<CurrencyInfoDto> updateCurrency(
            @PathVariable String currencyCode,
            @RequestBody CurrencyInfoDto dto) {
        return ResponseEntity.ok(currencyMappingService.update(currencyCode, dto));
    }

    /**
     * 刪除幣別
     *
     * @param currencyCode
     * @return
     */
    @DeleteMapping("/{currencyCode}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String currencyCode) {
        currencyMappingService.delete(currencyCode);
        return ResponseEntity.noContent().build();
    }
}
