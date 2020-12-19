package com.serdarormanli.sepet.server.repository;

import com.serdarormanli.sepet.server.model.Reading;
import com.serdarormanli.sepet.server.model.Statistic;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Set;

public interface ReadingRepository extends ReactiveCrudRepository<Reading, String> {

    @Query(value = "SELECT machine_id," +
            " MAX(temperature) AS maximum," +
            " MIN(temperature) AS minimum," +
            " AVG(temperature) AS average," +
            " percentile_cont(0.5) WITHIN GROUP (ORDER BY temperature) AS median" +
            " FROM readings" +
            " WHERE machine_id IN (:machineIds)" +
            " AND time BETWEEN :startDate AND :endDate" +
            " GROUP BY machine_id")
    Flux<Statistic> calculateStatistics(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate, @Param("machineIds") Set<String> machineIds);

    @Query("select distinct machine_id from readings")
    Flux<String> distinctMachineIds();
}
