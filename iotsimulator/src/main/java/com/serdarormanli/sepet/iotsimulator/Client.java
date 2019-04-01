package com.serdarormanli.sepet.iotsimulator;


import com.google.protobuf.Timestamp;
import com.serdarormanli.sepet.grpc.TemperatureReadingRequest;
import com.serdarormanli.sepet.grpc.TemperatureReadingResponse;
import com.serdarormanli.sepet.grpc.TemperatureServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.java.Log;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static java.util.logging.Level.SEVERE;

@Log
class Client {
    private final TemperatureServiceGrpc.TemperatureServiceStub stub;

    Client(String host, int port) {
        var channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        stub = TemperatureServiceGrpc.newStub(channel);
    }

    CompletableFuture<TemperatureReadingResponse> sendTemperatureReading(String machineId, double temperature) {
        var now = Instant.now();

        var temperatureReadingResponseFuture = new CompletableFuture<TemperatureReadingResponse>();

        var temperatureReadingResponse = new StreamObserver<TemperatureReadingResponse>() {
            @Override
            public void onNext(TemperatureReadingResponse temperatureReadingResponse) {
                temperatureReadingResponseFuture.complete(temperatureReadingResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                log.log(SEVERE, throwable, throwable::getMessage);
            }

            @Override
            public void onCompleted() {
            }
        };

        var temperatureReadingRequest = stub.sendTemperatureReading(temperatureReadingResponse);

        temperatureReadingRequest.onNext(TemperatureReadingRequest.newBuilder()
                .setMachineId(machineId)
                .setTemperature(temperature)
                .setTime(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .build());
        temperatureReadingRequest.onCompleted();

        return temperatureReadingResponseFuture;
    }
}