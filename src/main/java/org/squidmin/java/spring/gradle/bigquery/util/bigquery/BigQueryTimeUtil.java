package org.squidmin.java.spring.gradle.bigquery.util.bigquery;

import com.google.protobuf.Timestamp;
import com.google.type.DateTime;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class BigQueryTimeUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public String getCurrentDateTime() {
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).setNanos(0).build();
        DateTime dateTime = DateTime.newBuilder().setSeconds((int) timestamp.getSeconds()).setNanos(timestamp.getNanos()).build();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
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
