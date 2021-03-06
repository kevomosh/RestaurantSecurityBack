package com.example.updatedsecurity.controller;

import com.example.updatedsecurity.inputDTO.DateTimeInp;
import com.example.updatedsecurity.inputDTO.GenInp;
import com.example.updatedsecurity.inputDTO.LogInInp;
import com.example.updatedsecurity.inputDTO.RegisterInp;
import com.example.updatedsecurity.services.AuthService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterInp registerInp) {
        return authService.register(registerInp);
    }

    @PostMapping("/login")
    public String login(@RequestBody LogInInp logInInp) throws Exception {
        return authService.LogIn(logInInp);
    }

    @PostMapping("createPermission/{code}")
    public void createPermission(@PathVariable String code) {
         authService.createPermission(code);
    }

    @PostMapping("/{username}")
    public String addGroupToUser(@RequestBody GenInp genInp,
                                 @PathVariable String username) {
        return authService.addPermissionToUser(username, genInp);
    }

}
