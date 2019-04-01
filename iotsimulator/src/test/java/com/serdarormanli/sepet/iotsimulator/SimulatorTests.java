package com.serdarormanli.sepet.iotsimulator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimulatorTests {

    @Test
    public void temperatureGenerator() {
        var temperatureGenerator = new TemperatureGenerator(10);

        for (int i = 0; i < 1000; i++) {
            assertThat(temperatureGenerator.nextDouble()).isGreaterThanOrEqualTo(10);
        }
    }
}
