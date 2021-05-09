package com.example.updatedsecurity.repositories;

import com.example.updatedsecurity.Dto.CancelledResDTO;
import com.example.updatedsecurity.enums.ResStatusEnum;
import com.example.updatedsecurity.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT DISTINCT r FROM Reservation r JOIN FETCH " +
            "r.tables WHERE  r.sitting.id = :sitId ORDER BY r.startTime")
    List<Reservation> getAllBySittingId(@Param("sitId") Long sittingIt);

    @Query(value = "SELECT DISTINCT r FROM Reservation r JOIN FETCH " +
            "r.tables WHERE  r.startTime >= :start AND r.startTime <= :end ORDER BY r.startTime")
    List<Reservation> getAllBetweenTimes(@Param("start") OffsetDateTime sittingStart,
                                         @Param("end") OffsetDateTime sittingEnd);

    @Query(value = "SELECT new com.example.updatedsecurity.Dto.CancelledResDTO " +
            "(r.source, r.firstName, r.lastName, r.email, r.phoneNumber, r.startTime) " +
            "FROM Reservation r WHERE r.sitting.id = :sitId AND r.status = :status")
    List<CancelledResDTO> getCancelledBySittingId(@Param("sitId") Long sittingId,
                                                  @Param("status") ResStatusEnum resStatusEnum);

    @Query(value = "SELECT r FROM Reservation r JOIN FETCH r.tables WHERE r.id = :reservationId")
    Optional<Reservation> getById(@Param("reservationId") Long reservationId);

    @Query(value = "SELECT DISTINCT r.id FROM Reservation r WHERE r.sitting.id = :sitId ")
    List<Long> getIdsInSitting(@Param("sitId") Long sittingIt);
}
