package com.example.updatedsecurity.controller;

import com.example.updatedsecurity.enums.AreaEnum;
import com.example.updatedsecurity.enums.TableNumberEnum;
import com.example.updatedsecurity.model.Tables;
import com.example.updatedsecurity.services.TableService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables/")
@CrossOrigin
@AllArgsConstructor
public class TableController {
    private final TableService tableService;

    @GetMapping("all/{sittingId}")
    public ResponseEntity<Map<AreaEnum, List<Tables>>> allInSitting(@PathVariable Long sittingId) {
        return ResponseEntity.ok(tableService.getAllInSitting(sittingId));
    }

    @GetMapping("free/{sittingId}")
    public ResponseEntity<List<TableNumberEnum>> freeInSitting(@PathVariable Long sittingId) {
        return ResponseEntity.ok(tableService.getFreeInSitting(sittingId));
    }

}
