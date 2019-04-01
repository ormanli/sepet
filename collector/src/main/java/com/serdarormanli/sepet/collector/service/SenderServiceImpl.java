package com.serdarormanli.sepet.collector.service;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.grpc.TemperatureReadingRequest;
import com.serdarormanli.sepet.grpc.TemperatureReadingResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.Topic;

@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {

    private final RedisOperations<String, byte[]> redisOperations;
    private final Topic topic;
    private final TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();

    @Override
    public TemperatureReadingResponse send(TemperatureReadingRequest request) {
        var builder = TemperatureReadingResponse.newBuilder();

        if (StringUtils.isNotBlank(request.getMachineId())) {
            var id = timeBasedGenerator.generate().toString();

            var byteArray = TemperatureReading.newBuilder()
                    .setMessageId(id)
                    .setMachineId(request.getMachineId())
                    .setTemperature(request.getTemperature())
                    .setTime(request.getTime())
                    .build()
                    .toByteArray();

            redisOperations.convertAndSend(topic.getTopic(), byteArray);

            builder.setId(id);
        }

        return builder.build();
    }
}
