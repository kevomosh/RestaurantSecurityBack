package com.example.updatedsecurity.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInp {
    private String name;
    private String email;
    private String password;
    private String role;
}
