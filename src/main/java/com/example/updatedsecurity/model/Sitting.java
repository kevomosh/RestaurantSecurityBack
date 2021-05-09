package com.example.updatedsecurity.model;

import com.example.updatedsecurity.enums.CategoryEnum;
import com.example.updatedsecurity.inputDTO.SittingInp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Sitting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sitting_generator")
    @SequenceGenerator(name = "sitting_generator", sequenceName = "sitting_seq")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CategoryEnum category;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    private Integer capacity;

    private Boolean isClosed;

    @JsonIgnore
    @OneToMany(
            mappedBy = "sitting",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    public Sitting(SittingInp sittingInp, OffsetDateTime startTime,
                   OffsetDateTime endTime) {
        this.createOrUpdate(sittingInp, startTime, endTime);
        this.isClosed = false;
    }

    public void updateSitting(SittingInp sittingInp, OffsetDateTime startTime,
                              OffsetDateTime endTime) {
        this.createOrUpdate(sittingInp, startTime, endTime);
        this.isClosed = sittingInp.isClosed();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setSitting(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setSitting(null);
    }

    private void createOrUpdate(SittingInp sittingInp, OffsetDateTime startTime,
                                OffsetDateTime endTime) {
        this.category = CategoryEnum.valueOf(sittingInp.getCategoryStr());
        this.capacity = sittingInp.getCapacity();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sitting sitting = (Sitting) o;

        if (!getId().equals(sitting.getId())) return false;
        return getStartTime().equals(sitting.getStartTime());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getStartTime().hashCode();
        return result;
    }
}
