package com.example.updatedsecurity.inputDTO;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
}
