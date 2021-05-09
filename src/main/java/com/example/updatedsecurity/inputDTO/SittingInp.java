package com.example.updatedsecurity.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SittingInp {
    private Long id;

    @NotBlank(message = "Category is required")
    private String categoryStr;

    @NotNull
    private DateTimeInp startTime;

    @NotNull
    private DateTimeInp endTime;

    @Min(1)
    @Max(100)
    private int capacity;

    private boolean isClosed;
}
