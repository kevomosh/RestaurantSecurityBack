package com.example.updatedsecurity.Dto;

import com.example.updatedsecurity.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateCategoryDTO {
    private LocalDate date;
    private CategoryEnum category;
}
