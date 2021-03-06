package com.example.updatedsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin
public class TestController {
    @GetMapping("/user")
    public String userAccess(){
        return ">> User Contents";
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return ">>> Admin Contents";
    }

    @GetMapping("/admin/create")
    public String x() {return "admin create";}

    @GetMapping("/admin/delete")
    public String y() {return "admin delete";}
}
