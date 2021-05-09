package com.example.updatedsecurity.services;

import com.example.updatedsecurity.enums.AreaEnum;
import com.example.updatedsecurity.enums.TableNumberEnum;
import com.example.updatedsecurity.model.Tables;
import com.example.updatedsecurity.repositories.ReservationRepository;
import com.example.updatedsecurity.repositories.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TableService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public Map<AreaEnum, List<Tables>> getAllInSitting(Long sittingId){
        var resIdList = reservationRepository.getIdsInSitting(sittingId);
        return tableRepository.getAllInSitting(resIdList)
                .stream()
                .collect(Collectors.groupingBy(Tables::getArea));
    }

    public List<TableNumberEnum> getFreeInSitting(Long sittingId){
        var allTables = EnumSet.allOf(TableNumberEnum.class);
        var resIdList = reservationRepository.getIdsInSitting(sittingId);
        var bookedTables =  tableRepository.getAllInSitting(resIdList)
                .stream()
                .map(t -> t.getNumber())
                .collect(Collectors.toList());

        return allTables.stream()
                .filter(t -> !bookedTables.contains(t))
                .collect(Collectors.toList());
    }
}
