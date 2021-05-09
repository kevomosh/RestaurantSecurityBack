package com.example.updatedsecurity.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class StartEndTimeCapacityDTO {
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private int guestNumbers;
    private int availableCapacity;
}
