package com.jefferson.restful.h2.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class DateTimeUtils {

    /**
     * 時間轉換 string to LocalDateTime
     *
     * @param isoTime
     * @return
     */
    public LocalDateTime parseISOTime(String isoTime) {
        return Optional.ofNullable(isoTime)
                .map(time -> LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .orElse(LocalDateTime.now());
    }

    /**
     * 時間轉換 string to string
     *
     * @param isoTime
     * @return
     */
    public String formatUpdateTime(String isoTime) {
        try {
            // 解析ISO時間格式
            OffsetDateTime odt = OffsetDateTime.parse(isoTime);
            // 轉換成指定格式
            return odt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        } catch (Exception e) {
            // 如果解析失敗，返回原始時間
            return isoTime;
        }
    }
}
