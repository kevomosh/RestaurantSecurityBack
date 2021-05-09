package com.example.updatedsecurity.services;

import com.example.updatedsecurity.Dto.ResWithFreeTable;
import com.example.updatedsecurity.Dto.StartEndTimeCapacityDTO;
import com.example.updatedsecurity.enums.CategoryEnum;
import com.example.updatedsecurity.enums.ResStatusEnum;
import com.example.updatedsecurity.inputDTO.CreateResInp;
import com.example.updatedsecurity.inputDTO.DateTimeInp;
import com.example.updatedsecurity.model.Reservation;
import com.example.updatedsecurity.model.Sitting;
import com.example.updatedsecurity.repositories.ReservationRepository;
import com.example.updatedsecurity.repositories.SittingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SittingRepository sittingRepository;


    public ResWithFreeTable getAllBySittingId(Long sittingId) {
        if (!sittingRepository.existsById(sittingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sitting not available");
        }
        return getResWithFreeTables(sittingId);
    }

    public ResWithFreeTable update(CreateResInp createResInp) {
        if (Objects.isNull(createResInp.getReservationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sitting id is required");
        }
        var sitting = sittingRepository.findById(createResInp.getSittingId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sitting not available"));

        var reservation = reservationRepository.findById(createResInp.getReservationId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sitting not available"));

        var startEndTimeCapacityDto = checkAndGetStartEndTime(sitting, createResInp);

        reservation.updateReservation(createResInp, startEndTimeCapacityDto.getStartTime(),
                startEndTimeCapacityDto.getEndTime());

        if (startEndTimeCapacityDto.getGuestNumbers() == startEndTimeCapacityDto.getAvailableCapacity()) {
            sitting.setCategory(CategoryEnum.BOOKED_OUT);
        }

        reservationRepository.save(reservation);
        sittingRepository.save(sitting);

        return getResWithFreeTables(sitting.getId());
    }

    public ResWithFreeTable create(CreateResInp createResInp){
        var sitting = sittingRepository.findById(createResInp.getSittingId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sitting not available"));


        var startEndTimeCapacityDto = checkAndGetStartEndTime(sitting, createResInp);

        var reservation = new Reservation(createResInp.getResInp(),
                startEndTimeCapacityDto.getStartTime(), startEndTimeCapacityDto.getEndTime());

        reservation.addTables(createResInp.getTableInpSet());
        sitting.addReservation(reservation);

        if (startEndTimeCapacityDto.getGuestNumbers() == startEndTimeCapacityDto.getAvailableCapacity()) {
            sitting.setCategory(CategoryEnum.BOOKED_OUT);
        }
        sittingRepository.save(sitting);

        return getResWithFreeTables(sitting.getId());
    }

    public Reservation modifyState(Long reservationId, String action){
        var reservation = reservationRepository.getById(reservationId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not available"));

        switch (action) {
            case "confirm":
                reservation.confirm();
                break;
            case "seat":
                reservation.handleSeated();
                break;
            case "complete":
                reservation.handleCompleted();
                break;
            case "cancel":
                reservation.cancel();
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pick valid action");
        }

        reservationRepository.save(reservation);
        return reservation;
    }

    private ResWithFreeTable getResWithFreeTables(Long sittingId) {
        var reservations = reservationRepository.getAllBySittingId(sittingId);
        var cancelled = reservationRepository.getCancelledBySittingId(sittingId,
                ResStatusEnum.CANCELLED);
        return new ResWithFreeTable(reservations, cancelled);
    }


    private StartEndTimeCapacityDTO checkAndGetStartEndTime(Sitting sitting, CreateResInp createResInp){

        if (sitting.getCategory() == CategoryEnum.BOOKED_OUT ||
                sitting.getCategory() == CategoryEnum.PRIVATE_EVENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sitting is booked out or is Private event");
        }

        var currentCapacity = sittingRepository.getCurrentCapacity(sitting.getId()).orElse((long)0);
        var availableCapacity = sitting.getCapacity() - currentCapacity.intValue();

        var guestNumbers = createResInp.getResInp().getNoOfGuests();
        if ( guestNumbers > availableCapacity){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not " +
                    "enough available slots for the number of guests");
        }
        var resInput = createResInp.getResInp();

        var startTime = getOffsetDateTime(resInput.getStartTime());
        var endTime = startTime.plusHours(resInput.getDurHr()).plusMinutes(resInput.getDurMin());

        if (startTime.isBefore(sitting.getStartTime()) ||  startTime.isAfter(sitting.getEndTime())
                || endTime.isAfter(sitting.getEndTime().plusMinutes(30))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking is not within sitting times");
        }

        return new StartEndTimeCapacityDTO(startTime, endTime, guestNumbers, availableCapacity);
    }

    private OffsetDateTime getOffsetDateTime(DateTimeInp dateTimeInp) {
        ZoneId zoneId = ZoneId.of("Australia/Sydney");
        var localDateTime = LocalDateTime.of(dateTimeInp.getYear(), dateTimeInp.getMonth(),
                dateTimeInp.getDay(), dateTimeInp.getHour(), dateTimeInp.getMinute());
        var zoneOffset = localDateTime.atZone(zoneId).getOffset();
        return OffsetDateTime.of(localDateTime, zoneOffset);
    }
}
