package com.serdarormanli.sepet.server.listener;

import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.service.ReadingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@RequiredArgsConstructor
public class TemperatureListener implements MessageListener {

    private final ReadingService readingService;

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        var body = message.getBody();

        var temperatureReading = TemperatureReading.parseFrom(body);

        if (!temperatureReading.equals(TemperatureReading.getDefaultInstance())) {
            readingService.saveReading(temperatureReading);
        }
    }
}
