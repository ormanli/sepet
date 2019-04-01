package com.serdarormanli.sepet.server.service;

import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.dto.Query;
import com.serdarormanli.sepet.server.dto.ReadingStatistic;
import com.serdarormanli.sepet.server.model.Reading;

import java.util.List;
import java.util.Map;

public interface ReadingService {
    Reading saveReading(TemperatureReading temperatureReading);

    Map<String, ReadingStatistic> queryStatistics(Query query);

    List<String> distinctMachineIds();
}
