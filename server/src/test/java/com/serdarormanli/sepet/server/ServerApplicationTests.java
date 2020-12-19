package com.serdarormanli.sepet.server;

import com.google.protobuf.Timestamp;
import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.model.Reading;
import com.serdarormanli.sepet.server.repository.ReadingRepository;
import com.serdarormanli.sepet.server.service.ReadingService;
import com.serdarormanli.sepet.server.service.ReadingServiceImpl;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerApplicationTests {

    private static ReadingRepository readingRepository;
    private static ReadingService readingService;

    @BeforeClass
    public static void setUp() {
        readingRepository = mock(ReadingRepository.class);
        readingService = new ReadingServiceImpl(readingRepository);
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

        readingService.saveReading(reading);

        var expected = new Reading();
        expected.setTime(now);
        expected.setMachineId("1");
        expected.setTemperature(BigDecimal.valueOf(3.0));
        expected.setId("");

        verify(readingRepository, times(1)).save(refEq(expected, "id"));
    }

    @Test
    public void dontPersistEmptyMessageFromTopic() {
        var reading = TemperatureReading.newBuilder()
                .build();

        readingService.saveReading(reading);

        verify(readingRepository, never()).save(any());
    }
}

