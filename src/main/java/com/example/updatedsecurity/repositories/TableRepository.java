package com.example.updatedsecurity.repositories;

import com.example.updatedsecurity.model.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {
    @Query(value = "SELECT t FROM Tables t WHERE t.reservation.id IN (:resIdList)")
    List<Tables> getAllInSitting(@Param("resIdList") List<Long> resIdList);

    @Query(value = "SELECT t.id, t.area, t.number, t.status FROM Tables t WHERE " +
            "t.reservation.id IN (:resIdList)")
    Object geOrderedTablesInSitting(@Param("resIdList") List<Long> resIdList);
}
