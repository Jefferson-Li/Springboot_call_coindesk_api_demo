package com.jefferson.restful.h2.dto;

import com.jefferson.restful.h2.model.CurrencyInfo;

import java.util.List;

/**
 * 新 api 產出格式
 *
 * 此新 API 提供:
 * A. 更新時間(時間格式範例:1990/01/01 00:00:00)。 B. 幣別相關資訊(幣別，幣別中文名稱，以及匯率)。
 */
public class CurrencyInfoDto {

    /**
     * 更新時間(時間格式範例:1990/01/01 00:00:00)
     */
    private String updatedISOTime;

    /**
     * 幣別相關資訊(幣別，幣別中文名稱，以及匯率)
     */
    private List<CurrencyInfo> currencies;

    public String getUpdatedISOTime() {
        return updatedISOTime;
    }

    public void setUpdatedISOTime(String updatedISOTime) {
        this.updatedISOTime = updatedISOTime;
    }

    public List<CurrencyInfo> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyInfo> currencies) {
        this.currencies = currencies;
    }

}
