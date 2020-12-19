package com.serdarormanli.sepet.server.service;

import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.dto.Query;
import com.serdarormanli.sepet.server.dto.ReadingStatistic;
import com.serdarormanli.sepet.server.model.Reading;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface ReadingService {
    Mono<Reading> saveReading(@Valid TemperatureReading temperatureReading);

    Flux<ReadingStatistic> queryStatistics(Query query);

    Flux<String> distinctMachineIds();
}
