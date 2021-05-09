package com.example.updatedsecurity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SittingInpError {
    private String categoryStr;
    private OffsetDateTime startTime;
    private String message;


    public SittingInpError(String categoryStr, String message) {
        this.categoryStr = categoryStr;
        this.message = message;
    }

    public SittingInpError(String categoryStr, OffsetDateTime startTime, String message) {
        this.categoryStr = categoryStr;
        this.startTime = startTime;
        this.message = message;
    }


    @Override
    public String toString() {
        return "{" +
                "category='" + categoryStr + '\'' +
                ", startTime=" + startTime +
                ", message='" + message + '\'' +
                '}';
    }
}
