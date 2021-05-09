package com.example.updatedsecurity.inputDTO;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Data
public class DateTimeInp {
    @Max(2022)
    @Min(2021)
    private int year;

    @Max(12)
    @Min(1)
    private int month;

    @Min(1)
    @Max(31)
    private int day;

    @Min(0)
    @Max(23)
    private int hour;

    @Min(0)
    @Max(59)
    private int minute;

    public OffsetDateTime getOffSetDateTime() {
        ZoneId zoneId = ZoneId.of("Australia/Sydney");
        var localDateTime = LocalDateTime.of(this.getYear(), this.getMonth(),
                this.getDay(), this.getHour(), this.getMinute());
        var zoneOffset = localDateTime.atZone(zoneId).getOffset();
        return OffsetDateTime.of(localDateTime, zoneOffset);
    }

}
