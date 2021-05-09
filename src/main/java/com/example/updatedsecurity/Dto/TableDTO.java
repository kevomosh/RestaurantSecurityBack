package com.example.updatedsecurity.Dto;

import com.example.updatedsecurity.enums.AreaEnum;
import com.example.updatedsecurity.enums.TableNumberEnum;
import com.example.updatedsecurity.enums.TableStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableDTO {
    private AreaEnum area;
    private TableNumberEnum number;
    private TableStatusEnum status = TableStatusEnum.AVAILABLE;

    public TableDTO(TableNumberEnum tableNumberEnum){
        var strValue = tableNumberEnum.toString();
        switch (strValue.charAt(0)){
            case 'M':
                this.area = AreaEnum.MAIN;
                break;
            case 'O':
                this.area= AreaEnum.OUTSIDE;
                break;
            default:
                this.area = AreaEnum.BALCONY;
        }
        this.number = tableNumberEnum;
    }

}
