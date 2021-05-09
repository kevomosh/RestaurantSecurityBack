package com.example.updatedsecurity.inputDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TableInp {
    @NotBlank(message = "Area information Required")
    private String areaStr;

    @NotBlank(message = "Table number required")
    private String numberStr;
}
