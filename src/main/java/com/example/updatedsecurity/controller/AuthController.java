package com.example.updatedsecurity.controller;

import com.example.updatedsecurity.inpDTO.LogInInp;
import com.example.updatedsecurity.inpDTO.RegisterInp;
import com.example.updatedsecurity.model.Group;
import com.example.updatedsecurity.model.User;
import com.example.updatedsecurity.services.AuthService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("createGroup/{code}/{name}")
    public Group createGroup(@PathVariable String code,
                             @PathVariable String name) {
        return authService.addGroup(code, name);
    }
    @PostMapping("/{code}/{username}")
    public String addGroupToUser(@PathVariable String code,
                             @PathVariable String username) {
        return authService.addGroupToUser(username,code);
    }

    @GetMapping("/{name}")
    public User getUserByname(@PathVariable String name) {
        return authService.getUserByName(name);
    }
}
