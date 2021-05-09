package com.example.updatedsecurity.model;

import com.example.updatedsecurity.enums.AreaEnum;
import com.example.updatedsecurity.enums.TableNumberEnum;
import com.example.updatedsecurity.enums.TableStatusEnum;
import com.example.updatedsecurity.inputDTO.TableInp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
    @SequenceGenerator(name = "table_generator", sequenceName = "table_seq")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private AreaEnum area;

    @Enumerated(value = EnumType.STRING)
    private TableNumberEnum number;

    @Enumerated(value = EnumType.STRING)
    private TableStatusEnum status;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "res_id")
    private Reservation reservation;

    public Tables(TableInp tableInp) {
        this.area = AreaEnum.valueOf(tableInp.getAreaStr());
        this.number = TableNumberEnum.valueOf(tableInp.getNumberStr());
        this.status = TableStatusEnum.BOOKED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tables tables = (Tables) o;

        if (!getId().equals(tables.getId())) return false;
        return getNumber() == tables.getNumber();
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getNumber().hashCode();
        return result;
    }
}
