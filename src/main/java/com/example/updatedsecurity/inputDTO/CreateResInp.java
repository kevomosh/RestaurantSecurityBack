package com.example.updatedsecurity.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
public class CreateResInp {
    @NotNull(message = "reservation details required")
    private ResInp resInp;

    @Min(0)
    private long sittingId;

    @NotNull(message = "Table information required")
    private Set<TableInp> tableInpSet;

    private Long reservationId;
}
