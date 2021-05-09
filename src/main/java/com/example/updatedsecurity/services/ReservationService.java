package com.example.updatedsecurity.services;

import com.example.updatedsecurity.Dto.ResWithFreeTable;
import com.example.updatedsecurity.enums.CategoryEnum;
import com.example.updatedsecurity.enums.ResStatusEnum;
import com.example.updatedsecurity.inputDTO.CreateResInp;
import com.example.updatedsecurity.model.Reservation;
import com.example.updatedsecurity.model.Sitting;
import com.example.updatedsecurity.repositories.ReservationRepository;
import com.example.updatedsecurity.repositories.SittingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.Map;
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

        var capacityAndGuestNumbers = getCapacityAndGuestNumbers(sitting, createResInp);

        reservation.updateReservation(createResInp);

        if (capacityAndGuestNumbers.get("guestNumbers").equals(capacityAndGuestNumbers.get("availableCapacity"))) {
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


        var capacityAndGuestNumbers = getCapacityAndGuestNumbers(sitting, createResInp);

        var reservation = new Reservation(createResInp.getResInp());

        reservation.addTables(createResInp.getTableInpSet());
        sitting.addReservation(reservation);


        if (capacityAndGuestNumbers.get("guestNumbers").equals(capacityAndGuestNumbers.get("availableCapacity"))) {
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


    private Map<String, Integer> getCapacityAndGuestNumbers(Sitting sitting, CreateResInp createResInp){
        Map<String, Integer> res = new HashMap<>();

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

        var startTime = resInput.getStartTime().getOffSetDateTime();

        if (startTime.isBefore(sitting.getStartTime()) ||  startTime.isAfter(sitting.getEndTime())
                || resInput.getEndTime().isAfter(sitting.getEndTime().plusMinutes(30))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking is not within sitting times");
        }
        res.put("guestNumbers", guestNumbers);
        res.put("availableCapacity", availableCapacity);
        return res;
    }

}
