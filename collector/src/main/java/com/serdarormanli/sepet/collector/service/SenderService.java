package com.serdarormanli.sepet.collector.service;

import com.serdarormanli.sepet.grpc.TemperatureReadingRequest;
import com.serdarormanli.sepet.grpc.TemperatureReadingResponse;

public interface SenderService {
    TemperatureReadingResponse send(TemperatureReadingRequest request);
}
