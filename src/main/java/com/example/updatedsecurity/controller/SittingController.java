package com.example.updatedsecurity.controller;

import com.example.updatedsecurity.inputDTO.DateTimeInp;
import com.example.updatedsecurity.inputDTO.SittingInp;
import com.example.updatedsecurity.inputDTO.SittingInpSet;
import com.example.updatedsecurity.model.Sitting;
import com.example.updatedsecurity.services.SittingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sitting")
@CrossOrigin
@AllArgsConstructor
public class SittingController {
    private final SittingService sittingService;


    @GetMapping()
    public ResponseEntity<List<Sitting>> getAllSittings(){
        return ResponseEntity.ok(sittingService.getAll());
    }

    @GetMapping({"/{sittingId}"})
    public ResponseEntity<Sitting> getById(@PathVariable Long sittingId) {
        return ResponseEntity.ok(sittingService.getById(sittingId));
    }
    @PostMapping()
    public ResponseEntity<String> createSitting(@Valid @RequestBody SittingInpSet sittingInpSet){
        return ResponseEntity.ok(sittingService.create(sittingInpSet));
    }

    @PostMapping("/day")
    public ResponseEntity<List<Sitting>> getSittingsOfDay(@Valid @RequestBody DateTimeInp dateTimeInp) {
        return ResponseEntity.ok(sittingService.getSittingsOfDay(dateTimeInp));
    }

    @PutMapping()
    public ResponseEntity<Sitting>update(@Valid @RequestBody SittingInp sittingInp) {
        return ResponseEntity.ok(sittingService.update(sittingInp));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(sittingService.delete(id));
    }
}
