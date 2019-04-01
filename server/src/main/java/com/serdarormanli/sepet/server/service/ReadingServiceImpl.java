package com.serdarormanli.sepet.server.service;

import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.dto.Query;
import com.serdarormanli.sepet.server.dto.ReadingStatistic;
import com.serdarormanli.sepet.server.model.Reading;
import com.serdarormanli.sepet.server.model.ReadingKey;
import com.serdarormanli.sepet.server.model.Statistic;
import com.serdarormanli.sepet.server.repository.ReadingRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.Instant.ofEpochSecond;

@RequiredArgsConstructor
public class ReadingServiceImpl implements ReadingService {

    private final ReadingRepository readingRepository;

    @Override
    public Reading saveReading(@NonNull TemperatureReading temperatureReading) {
        var reading = new Reading();
        var readingKey = new ReadingKey();

        readingKey.setMachineId(temperatureReading.getMachineId());
        readingKey.setTime(ofEpochSecond(temperatureReading.getTime().getSeconds(), temperatureReading.getTime().getNanos()));
        reading.setReadingKey(readingKey);
        reading.setTemperature(BigDecimal.valueOf(temperatureReading.getTemperature()));

        return readingRepository.saveAndFlush(reading);
    }

    @Override
    public Map<String, ReadingStatistic> queryStatistics(@NonNull @Valid Query query) {
        var statistics = readingRepository.calculateStatistics(query.getStartDate(), query.getEndDate(), query.getMachineIds());

        return statistics.stream()
                .collect(Collectors.toMap(Statistic::getMachineId, this::readingStatistic));
    }

    @Override
    public List<String> distinctMachineIds() {
        return readingRepository.distinctMachineIds();
    }

    private ReadingStatistic readingStatistic(Statistic statistic) {
        return new ReadingStatistic(statistic.getMinimum(), statistic.getMaximum(), statistic.getAverage(), statistic.getMedian());
    }
}
