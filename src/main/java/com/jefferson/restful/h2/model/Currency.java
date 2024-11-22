package com.jefferson.restful.h2.model;

import java.math.BigDecimal;

/**
 * coindesk API Bean
 */
public class Currency {
    private String code;
    private String symbol;
    private String rate;
    private String description;
    private BigDecimal rate_float;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRate_float() {
        return rate_float;
    }

    public void setRate_float(BigDecimal rate_float) {
        this.rate_float = rate_float;
    }
}
