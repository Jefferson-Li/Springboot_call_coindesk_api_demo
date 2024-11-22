package com.jefferson.restful.h2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * coindesk API Bean
 */
public class BPI {

    @JsonProperty("USD")
    private Currency usd;

    @JsonProperty("GBP")
    private Currency gbp;

    @JsonProperty("EUR")
    private Currency eur;

    public Currency getUsd() {
        return usd;
    }

    public void setUsd(Currency usd) {
        this.usd = usd;
    }

    public Currency getGbp() {
        return gbp;
    }

    public void setGbp(Currency gbp) {
        this.gbp = gbp;
    }

    public Currency getEur() {
        return eur;
    }

    public void setEur(Currency eur) {
        this.eur = eur;
    }
}
