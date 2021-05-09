package com.example.updatedsecurity.Dto;

import com.example.updatedsecurity.enums.SourceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class CancelledResDTO {
    private SourceEnum source;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private OffsetDateTime startTime;

}
