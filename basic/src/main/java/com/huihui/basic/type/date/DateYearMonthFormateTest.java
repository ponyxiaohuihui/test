package com.huihui.basic.type.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateYearMonthFormateTest {
    public static void main(String[] args) {
        DateTimeFormatter fomatter3 = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse("201012", fomatter3);
        LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.ofHours(0));
        Date date = Date.from(instant);
        System.out.println(date);
    }
}
