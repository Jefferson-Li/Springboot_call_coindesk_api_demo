package com.jefferson.restful.h2.controller;

import com.jefferson.restful.h2.dto.BitcoinPriceResponse;
import com.jefferson.restful.h2.dto.CurrencyInfoDto;
import com.jefferson.restful.h2.service.CoindeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CoindeskController {


    private final CoindeskService coindeskService;

    @Autowired
    public CoindeskController(CoindeskService coindeskService) {
        this.coindeskService = coindeskService;
    }

    /**
     * 呼叫 coinDesk 的API。
     *
     * @return
     */
    @GetMapping("/coinDesk/currentPrice")
    public ResponseEntity<BitcoinPriceResponse> getCoindeskApiRes() {
        return ResponseEntity.ok(coindeskService.getCoindeskApiRes());
    }

    /**
     * 呼叫 coinDesk 的API，並進行資料轉換，組成新API。
     *
     * @return
     */
    @GetMapping("/coinDesk/currentPrice/transToNewApi")
    public ResponseEntity<CurrencyInfoDto> getCoinDeskCurrentPriceTransToNewApi() {
        CurrencyInfoDto response = coindeskService.getCoinDeskCurrentPriceTransToNewApi();
        return ResponseEntity.ok(response);
    }
}
