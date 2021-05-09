package com.example.updatedsecurity.inputDTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SittingInpSet {
    @NotNull
    private Set<SittingInp> sittingInpSet;
}
