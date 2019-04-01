package com.serdarormanli.sepet.collector;

import com.google.protobuf.Timestamp;
import com.serdarormanli.sepet.collector.service.SenderService;
import com.serdarormanli.sepet.collector.service.SenderServiceImpl;
import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.grpc.TemperatureReadingRequest;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class CollectorApplicationTests {

    private static RedisOperations<String, byte[]> redisOperations;
    private static SenderService senderService;

    @BeforeClass
    public static void setUp() {
        redisOperations = (RedisOperations<String, byte[]>) mock(RedisOperations.class);
        senderService = new SenderServiceImpl(redisOperations, ChannelTopic.of("test"));
    }

    @After
    public void tearDown() {
        reset(redisOperations);
    }

    @Test
    @SneakyThrows
    public void sendValidReading() {
        var now = Instant.now();

        var temperatureReadingResponse = senderService.send(TemperatureReadingRequest.newBuilder()
                .setTime(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano()).build())
                .setMachineId("1")
                .setTemperature(2)
                .build());

        assertThat(temperatureReadingResponse.getId()).isNotBlank();

        var byteArrayCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(redisOperations).convertAndSend(anyString(), byteArrayCaptor.capture());

        var capturedValues = byteArrayCaptor.getAllValues();
        assertThat(capturedValues).hasSize(1);

        var temperatureReading = TemperatureReading.parseFrom(capturedValues.get(0));

        assertThat(Instant.ofEpochSecond(temperatureReading.getTime().getSeconds(), temperatureReading.getTime().getNanos())).isEqualTo(now);
        assertThat(temperatureReading.getMachineId()).isEqualTo("1");
        assertThat(temperatureReading.getMessageId()).isEqualTo(temperatureReadingResponse.getId());
        assertThat(temperatureReading.getTemperature()).isCloseTo(2, within(0.1));
    }

    @Test
    @SneakyThrows
    public void sendInvalidReading() {
        var now = Instant.now();

        var temperatureReadingResponse = senderService.send(TemperatureReadingRequest.newBuilder()
                .setTime(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano()).build())
                .setTemperature(2)
                .build());

        assertThat(temperatureReadingResponse.getId()).isBlank();

        var byteArrayCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(redisOperations, never()).convertAndSend(anyString(), byteArrayCaptor.capture());

        var capturedValues = byteArrayCaptor.getAllValues();
        assertThat(capturedValues).isEmpty();
    }
}

