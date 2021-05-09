package com.example.updatedsecurity.repositories;

import com.example.updatedsecurity.model.Sitting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SittingRepository extends JpaRepository<Sitting, Long> {
    @Query(value = "select exists(select id from sitting where start_time > :startOfDay " +
            "and start_time < :endOfDay and category = :categoryStr)", nativeQuery = true)
    boolean existsOnDayAndCategory(@Param("startOfDay") OffsetDateTime startOfDay,
                                   @Param("endOfDay") OffsetDateTime endOfDay,
                                   @Param("categoryStr") String categoryStr);

    @Query(value = "select exists(select id from sitting where start_time > :startOfDay " +
            "and start_time < :endOfDay and category = :categoryStr and id <> :sittingId)", nativeQuery = true)
    boolean existsOnDayWithCategoryAndIdNot(@Param("startOfDay")OffsetDateTime startOfDay,
                                            @Param("endOfDay") OffsetDateTime endOfDay,
                                            @Param("categoryStr") String categoryStr,
                                            @Param("sittingId") Long sittingId);

    @Query(value = "SELECT SUM(no_of_guests) FROM reservation r" +
            " WHERE r.sitting_id = :sitId AND r.status != 'CANCELLED'"
            ,nativeQuery = true)
    Optional<Long> getCurrentCapacity(@Param("sitId") Long sittingId);

    @Query(value = "select s from Sitting s where" +
            " s.startTime > :startOfDay and s.startTime < :endOfDay")
    List<Sitting> getAllOnDay(@Param("startOfDay")OffsetDateTime startOfDay,
                              @Param("endOfDay") OffsetDateTime endOfDay);
}
