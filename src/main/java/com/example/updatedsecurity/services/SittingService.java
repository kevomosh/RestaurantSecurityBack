package com.example.updatedsecurity.services;

import com.example.updatedsecurity.Dto.SittingInpError;
import com.example.updatedsecurity.enums.CategoryEnum;
import com.example.updatedsecurity.inputDTO.DateTimeInp;
import com.example.updatedsecurity.inputDTO.SittingInp;
import com.example.updatedsecurity.inputDTO.SittingInpSet;
import com.example.updatedsecurity.model.Sitting;
import com.example.updatedsecurity.repositories.SittingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SittingService {
    private final SittingRepository sittingRepository;


    public String create(SittingInpSet sittingInpSet) {
        List<SittingInpError> errorsList = new ArrayList<>();

        for (SittingInp sittingInp : sittingInpSet.getSittingInpSet()) {
            var categoryStr = getCategoryStr(sittingInp.getCategoryStr());

            if (categoryStr.equals("Illegal Category")) {
                errorsList.add(new SittingInpError(sittingInp.getCategoryStr(), "supplied category" +
                        " doesn't exist"));
                break;
            }

            var startEndOfDay = getStartAndEndOfDay(sittingInp.getStartTime());
            var sittingExists =
                    sittingRepository.existsOnDayAndCategory(startEndOfDay.get("startOfDay"),
                            startEndOfDay.get("endOfDay"), categoryStr);
            var startTime = getOffsetDateTime(sittingInp.getStartTime());
            var endTime = getOffsetDateTime(sittingInp.getEndTime());

            if (sittingExists) {
                errorsList.add(new SittingInpError(categoryStr, startTime, "Sitting already exists"));
                break;
            }

            var sitting = new Sitting(sittingInp, startTime, endTime);
            sittingRepository.save(sitting);

        }

        if (!errorsList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Following sittings have not been added" +
                    " " + errorsList);
        }

        return "All added";

    }

    public String delete(Long id) {
        var sitting = sittingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sitting not present")
        );
        sittingRepository.delete(sitting);
        return "Deleted";
    }

    public Sitting update(SittingInp sittingInp) {
        var categoryStr = getCategoryStr(sittingInp.getCategoryStr());

        if (categoryStr.equals("Illegal Category")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid category");
        }

        var sitting = sittingRepository.findById(sittingInp.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Sitting not present"));

        var startEndOfDay = getStartAndEndOfDay(sittingInp.getStartTime());

        var sittingExists =
                sittingRepository.existsOnDayWithCategoryAndIdNot(startEndOfDay.get("startOfDay"),
                        startEndOfDay.get("endOfDay"), categoryStr, sitting.getId());

        if (sittingExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Theres already a sitting " +
                    "with same category on the day, change date or category");
        }
        var startTime = getOffsetDateTime(sittingInp.getStartTime());
        var endTime = getOffsetDateTime(sittingInp.getEndTime());

        sitting.updateSitting(sittingInp, startTime, endTime);

        sittingRepository.save(sitting);
        return sitting;
    }

    public Sitting getById(Long sittingId) {
        return sittingRepository.findById(sittingId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not present"));
    }

    public List<Sitting> getAll() {
        return sittingRepository.findAll();
    }


    public List<Sitting> getSittingsOfDay(DateTimeInp dateTimeInp) {
        var startEndOfDay = getStartAndEndOfDay(dateTimeInp);

        return sittingRepository.getAllOnDay(startEndOfDay.get("startOfDay"),
                startEndOfDay.get("endOfDay"));
    }

    private String getCategoryStr(String categoryStr) {
        try {
            var response = CategoryEnum.valueOf(categoryStr);
            return response.toString();
        } catch (IllegalArgumentException e) {
            return "Illegal Category";
        }
    }

    private Map<String, OffsetDateTime> getStartAndEndOfDay(DateTimeInp startTime) {
        Map<String, OffsetDateTime> response = new HashMap<>();
        var actualDate = getOffsetDateTime(startTime);
        var hrs = actualDate.getHour();
        var min = actualDate.getMinute();
        var startOfDay = actualDate.minusHours(hrs).minusMinutes(min);
        var endOfDay = startOfDay.plusDays(1);

        response.put("startOfDay", startOfDay);
        response.put("endOfDay", endOfDay);
        return response;
    }

    private OffsetDateTime getOffsetDateTime(DateTimeInp dateTimeInp) {
        ZoneId zoneId = ZoneId.of("Australia/Sydney");
        var localDateTime = LocalDateTime.of(dateTimeInp.getYear(), dateTimeInp.getMonth(),
                dateTimeInp.getDay(), dateTimeInp.getHour(), dateTimeInp.getMinute());
        var zoneOffset = localDateTime.atZone(zoneId).getOffset();
        return OffsetDateTime.of(localDateTime, zoneOffset);
    }
}
