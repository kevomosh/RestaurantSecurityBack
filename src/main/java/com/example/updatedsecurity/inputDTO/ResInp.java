package com.example.updatedsecurity.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
public class ResInp {
    @NotBlank(message = "Reservation source is required")
    private String sourceStr;

    @NotBlank(message = "Firstname is required")
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 7, max = 50)
    private String phoneNumber;

    @NotBlank
    private DateTimeInp startTime;

    @Max(value = 5, message = "Reservation cant be more than 5 hours")
    @Min(0)
    private long durHr;

    @Max(value = 59)
    @Min(0)
    private long durMin;

    @Min(0)
    private int noOfGuests;

    private String notes;

}
