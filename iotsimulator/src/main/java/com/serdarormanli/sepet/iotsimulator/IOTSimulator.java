package com.serdarormanli.sepet.iotsimulator;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import static java.util.logging.Level.SEVERE;

@RequiredArgsConstructor
@Log
public class IOTSimulator implements Runnable {

    private final String id;
    private final Client client;
    private final TemperatureGenerator temperatureGenerator;

    @Override
    public void run() {
        try {
            var response = client.sendTemperatureReading(id, temperatureGenerator.nextDouble());

            response.thenAccept(temperatureReadingResponse -> {
                //log.info(String.format("I am %s and response is %s", id, temperatureReadingResponse.getId()));
            });
        } catch (Exception e) {
            log.log(SEVERE, e, e::getMessage);
        }
    }
}
