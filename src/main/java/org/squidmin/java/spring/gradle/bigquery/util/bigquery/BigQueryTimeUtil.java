package org.squidmin.java.spring.gradle.bigquery.util.bigquery;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class BigQueryTimeUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return now.format(formatter);
    }

    public String getStartOfCurrentDateTime() {
        LocalDate currentDate = LocalDate.now();
        Date startOfDay = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(startOfDay);
    }

    public String getEndOfCurrentDateTime() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime endOfDay = currentDate.atTime(LocalTime.MAX);
        Date endOfDayAsDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(endOfDayAsDate);
    }

}
