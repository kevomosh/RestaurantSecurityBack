package com.example.updatedsecurity.Dto;

import com.example.updatedsecurity.enums.TableNumberEnum;
import com.example.updatedsecurity.enums.TableStatusEnum;
import com.example.updatedsecurity.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResWithFreeTable {
    private List<Reservation> reservations;
    private List<CancelledResDTO> cancelled;
    private List<TableDTO>freeTables;

    public ResWithFreeTable(List<Reservation> reservations,List<CancelledResDTO> cancelled ) {
        this.reservations = reservations;
        this.freeTables = computeFreeTables();
        this.cancelled = cancelled;
    }


    private List<TableDTO> computeFreeTables(){
        var tablesInRes = this.reservations
                .stream()
                .flatMap(reservation -> reservation
                        .getTables()
                        .stream()
                        .filter(t -> t.getStatus() != TableStatusEnum.AVAILABLE)
                        .map(table -> table.getNumber())
                )
                .collect(Collectors.toList());

        return EnumSet.allOf(TableNumberEnum.class)
                .stream()
                .filter(table -> !tablesInRes.contains(table))
                .map(TableDTO::new)
                .collect(Collectors.toList());
    }
}
