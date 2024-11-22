package com.jefferson.restful.h2.dto;

import com.jefferson.restful.h2.model.BPI;
import com.jefferson.restful.h2.model.Time;

/**
 * coindesk API Bean
 */
public class BitcoinPriceResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private BPI bpi;

    // Getters and Setters
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public BPI getBpi() {
        return bpi;
    }

    public void setBpi(BPI bpi) {
        this.bpi = bpi;
    }
}
