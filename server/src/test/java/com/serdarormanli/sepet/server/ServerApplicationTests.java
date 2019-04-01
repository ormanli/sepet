package com.serdarormanli.sepet.server;

import com.google.protobuf.Timestamp;
import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.listener.TemperatureListener;
import com.serdarormanli.sepet.server.model.Reading;
import com.serdarormanli.sepet.server.model.ReadingKey;
import com.serdarormanli.sepet.server.repository.ReadingRepository;
import com.serdarormanli.sepet.server.service.ReadingServiceImpl;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.redis.connection.DefaultMessage;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerApplicationTests {

    private static ReadingRepository readingRepository;
    private static TemperatureListener temperatureListener;

    @BeforeClass
    public static void setUp() {
        readingRepository = mock(ReadingRepository.class);
        temperatureListener = new TemperatureListener(new ReadingServiceImpl(readingRepository));
    }

    @After
    public void tearDown() {
        reset(readingRepository);
    }

    @Test
    public void persistFromTopic() {
        var now = Instant.now();

        var reading = TemperatureReading.newBuilder()
                .setTime(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .setMachineId("1")
                .setTemperature(3)
                .build();

        temperatureListener.onMessage(new DefaultMessage(new byte[0], reading.toByteArray()), null);

        var expected = new Reading();
        var readingKey = new ReadingKey();
        readingKey.setTime(now);
        readingKey.setMachineId("1");
        expected.setTemperature(BigDecimal.valueOf(3.0));
        expected.setReadingKey(readingKey);

        verify(readingRepository, times(1)).saveAndFlush(eq(expected));
    }

    @Test
    public void dontPersistEmptyMessageFromTopic() {
        var reading = TemperatureReading.newBuilder()
                .build();

        temperatureListener.onMessage(new DefaultMessage(new byte[0], reading.toByteArray()), null);

        verify(readingRepository, never()).saveAndFlush(any());
    }
}

