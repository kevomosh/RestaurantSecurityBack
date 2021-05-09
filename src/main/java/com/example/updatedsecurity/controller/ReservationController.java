package com.example.updatedsecurity.controller;


import com.example.updatedsecurity.Dto.ResWithFreeTable;
import com.example.updatedsecurity.inputDTO.CreateResInp;
import com.example.updatedsecurity.model.Reservation;
import com.example.updatedsecurity.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;


    @PostMapping()
    public ResponseEntity<ResWithFreeTable> create(
            @Valid @RequestBody CreateResInp createResInp){
        return ResponseEntity.ok(reservationService.create(createResInp));
    }

    @PutMapping()
    public ResponseEntity<ResWithFreeTable> update(
            @Valid @RequestBody CreateResInp createResInp){
        return ResponseEntity.ok(reservationService.update(createResInp));
    }

    @PatchMapping("/{action}/{reservationId}")
    public ResponseEntity<Reservation>  modifyState(
            @PathVariable Long reservationId, @PathVariable String action) {
        return ResponseEntity.ok(reservationService.modifyState(reservationId, action));
    }


    @GetMapping("/{sittingId}")
    public ResponseEntity<ResWithFreeTable> bySittingId(
            @PathVariable Long sittingId
    ){
        return ResponseEntity.ok(reservationService.getAllBySittingId(sittingId));
    }
}
