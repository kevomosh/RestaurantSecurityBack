package com.example.updatedsecurity.inpDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInInp {
    private String email;
    private String password;

}
