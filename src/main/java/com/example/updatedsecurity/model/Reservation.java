package com.example.updatedsecurity.model;

import com.example.updatedsecurity.enums.ResStatusEnum;
import com.example.updatedsecurity.enums.SourceEnum;
import com.example.updatedsecurity.enums.TableStatusEnum;
import com.example.updatedsecurity.inputDTO.CreateResInp;
import com.example.updatedsecurity.inputDTO.ResInp;
import com.example.updatedsecurity.inputDTO.TableInp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "res_generator")
    @SequenceGenerator(name = "res_generator", sequenceName = "reservation_seq")
    private Long id;


    @Enumerated(value = EnumType.STRING)
    private SourceEnum source;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    private Integer noOfGuests;

    private String notes;

    private OffsetDateTime actualStartTime;

    private OffsetDateTime actualEndTime;

    @Enumerated(value = EnumType.STRING)
    private ResStatusEnum status;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "sitting_id")
    private Sitting sitting;

    @OneToMany(
            mappedBy = "reservation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Tables> tables = new ArrayList<>();

    public Reservation(ResInp info,   OffsetDateTime startTime, OffsetDateTime endTime){
        createOrUpdateReservation(info, startTime, endTime);
    }

    public void updateReservation(CreateResInp createResInp, OffsetDateTime startTime, OffsetDateTime endTime) {
        createOrUpdateReservation(createResInp.getResInp(), startTime, endTime);

        var newTableNumberList = createResInp.getTableInpSet()
                .stream()
                .map(t -> t.getNumberStr())
                .collect(Collectors.toList());

        var currentNumberList =
                tables.stream()
                        .map(t -> t.getNumber().toString())
                        .collect(Collectors.toList());

        var partitioned = tables.stream()
                .collect(Collectors.partitioningBy(t -> newTableNumberList.contains(t.getNumber().toString())));

        tables.removeAll(partitioned.get(false));

        createResInp.getTableInpSet()
                .stream()
                .filter(t -> !currentNumberList.contains(t.getNumberStr()))
                .map(Tables::new)
                .forEach(tableToAdd -> {
                    tables.add(tableToAdd);
                    tableToAdd.setReservation(this);
                });
    }

    private void createOrUpdateReservation(ResInp info,
                                           OffsetDateTime startTime, OffsetDateTime endTime){
        this.source = SourceEnum.valueOf(info.getSourceStr());
        this.firstName = info.getFirstName();
        this.lastName = info.getLastName();
        this.email = info.getEmail();
        this.phoneNumber = info.getPhoneNumber();
        this.startTime = startTime;
        this.endTime =  endTime;
        this.noOfGuests = info.getNoOfGuests();
        this.notes = info.getNotes();
        this.status = ResStatusEnum.PENDING;
    }

    public void addTables(Set<TableInp> tableInpSet){
        for (TableInp tableInp : tableInpSet) {
            var table = new Tables(tableInp);
            tables.add(table);
            table.setReservation(this);
        }
    }
    public void removeTable(Tables table){
        tables.remove(table);
        table.setReservation(null);
    }

    public void confirm(){
        if (getStatus() != ResStatusEnum.CONFIRMED) {
            setStatus(ResStatusEnum.CONFIRMED);
            for (Tables table: getTables()){
                table.setStatus(TableStatusEnum.CONFIRMED_BOOKING);
            }
        }
    }

    public void cancel(){
        if (getStatus() != ResStatusEnum.CANCELLED) {
            setStatus(ResStatusEnum.CANCELLED);
            this.tables.clear();
        }
    }
    public void handleSeated(){
        var currentStatus = getStatus();
        if (currentStatus != ResStatusEnum.SEATED) {
            setStatus(ResStatusEnum.SEATED);
            setActualStartTime(OffsetDateTime.now());
            for (Tables table: getTables()){
                table.setStatus(TableStatusEnum.OCCUPIED);
            }
        }
    }

    public void handleCompleted(){
        if (getStatus() != ResStatusEnum.COMPLETED) {
            setStatus(ResStatusEnum.COMPLETED);
            setActualEndTime(OffsetDateTime.now());
            for (Tables table: getTables()){
                table.setStatus(TableStatusEnum.AVAILABLE);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (!getId().equals(that.getId())) return false;
        if (!getFirstName().equals(that.getFirstName())) return false;
        return getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }
}
