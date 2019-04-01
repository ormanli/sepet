package com.serdarormanli.sepet.server.repository;

import com.serdarormanli.sepet.server.model.Reading;
import com.serdarormanli.sepet.server.model.ReadingKey;
import com.serdarormanli.sepet.server.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface ReadingRepository extends JpaRepository<Reading, ReadingKey> {

    @Query(value = "SELECT machine_id AS machineId," +
            " MAX(temperature) AS maximum," +
            " MIN(temperature) AS minimum," +
            " AVG(temperature) AS average," +
            " percentile_cont(0.5) WITHIN GROUP (ORDER BY temperature) AS median" +
            " FROM readings" +
            " WHERE machine_id IN (:machineIds)" +
            " AND time BETWEEN :startDate AND :endDate" +
            " GROUP BY machine_id", nativeQuery = true)
    List<Statistic> calculateStatistics(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate, @Param("machineIds") Set<String> machineIds);

    @Query("select distinct reading.readingKey.machineId from Reading reading")
    List<String> distinctMachineIds();
}
