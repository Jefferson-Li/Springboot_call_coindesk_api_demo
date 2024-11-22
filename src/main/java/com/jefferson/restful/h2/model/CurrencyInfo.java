package com.jefferson.restful.h2.model;

/**
 * 新 api 產出格式
 *
 * 此新 API 提供:
 * A. 更新時間(時間格式範例:1990/01/01 00:00:00)。 B. 幣別相關資訊(幣別，幣別中文名稱，以及匯率)。
 */
public class CurrencyInfo {

    /**
     * 幣別
     */
    private String currencyCode;

    /**
     * 幣別中文名稱
     */
    private String chineseName;

    /**
     * 匯率
     */
    private String rate;

    /**
     * 異動該幣別表的時間
     */
    private String updatedTime;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

}
