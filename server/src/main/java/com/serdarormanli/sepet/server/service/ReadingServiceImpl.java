package com.serdarormanli.sepet.server.service;

import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.dto.Query;
import com.serdarormanli.sepet.server.dto.ReadingStatistic;
import com.serdarormanli.sepet.server.model.Reading;
import com.serdarormanli.sepet.server.model.Statistic;
import com.serdarormanli.sepet.server.repository.ReadingRepository;
import de.huxhorn.sulky.ulid.ULID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.math.BigDecimal.valueOf;
import static java.time.Instant.ofEpochSecond;

@RequiredArgsConstructor
@Validated
public class ReadingServiceImpl implements ReadingService {

    private final ReadingRepository readingRepository;
    private final ULID ulid = new ULID();

    @Override
    public Mono<Reading> saveReading(@NonNull TemperatureReading temperatureReading) {
        if (TemperatureReading.getDefaultInstance().equals(temperatureReading)) {
            return Mono.empty();
        }

        var reading = new Reading();

        reading.setId(this.ulid.nextULID());
        reading.setMachineId(temperatureReading.getMachineId());
        reading.setTime(ofEpochSecond(temperatureReading.getTime().getSeconds(), temperatureReading.getTime().getNanos()));
        reading.setTemperature(valueOf(temperatureReading.getTemperature()));

        return this.readingRepository.save(reading);
    }

    @Override
    public Flux<ReadingStatistic> queryStatistics(@NonNull Query query) {
        return this.readingRepository.calculateStatistics(query.getStartDate(), query.getEndDate(), query.getMachineIds())
                .map(this::readingStatistic);
    }

    @Override
    public Flux<String> distinctMachineIds() {
        return this.readingRepository.distinctMachineIds();
    }

    private ReadingStatistic readingStatistic(Statistic statistic) {
        return new ReadingStatistic(statistic.getMachineId(),
                statistic.getMinimum(),
                statistic.getMaximum(),
                statistic.getAverage(),
                statistic.getMedian());
    }
}
