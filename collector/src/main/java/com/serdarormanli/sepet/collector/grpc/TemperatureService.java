package com.serdarormanli.sepet.collector.grpc;

import com.serdarormanli.sepet.collector.service.SenderService;
import com.serdarormanli.sepet.grpc.TemperatureReadingRequest;
import com.serdarormanli.sepet.grpc.TemperatureReadingResponse;
import com.serdarormanli.sepet.grpc.TemperatureServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TemperatureService extends TemperatureServiceGrpc.TemperatureServiceImplBase {

    private final SenderService senderService;

    @Override
    public StreamObserver<TemperatureReadingRequest> sendTemperatureReading(StreamObserver<TemperatureReadingResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(TemperatureReadingRequest temperatureReadingRequest) {
                var temperatureReadingResponse = TemperatureService.this.senderService.send(temperatureReadingRequest);

                responseObserver.onNext(temperatureReadingResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
            }
        };
    }
}
