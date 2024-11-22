package com.jefferson.restful.h2.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "CURRENCY_MAPPING")
public class CurrencyMapping {
    @Id
    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;

    @Column(name = "CHINESE_NAME", nullable = false)
    private String chineseName;

    @Column(name = "RATE", nullable = false)
    private String rate;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "UPDATED_ISO_TIME")
    private LocalDateTime updatedISOTime;

    @Column(name = "UPDATED_TIME")
    private LocalDateTime updatedTime;

    public String getFormattedUpdateTime() {
        return updatedISOTime != null ?
                updatedISOTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : null;
    }

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

    public LocalDateTime getUpdatedISOTime() {
        return updatedISOTime;
    }

    public void setUpdatedISOTime(LocalDateTime updatedISOTime) {
        this.updatedISOTime = updatedISOTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
