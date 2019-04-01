package com.serdarormanli.sepet.iotsimulator;

import lombok.extern.java.Log;
import picocli.CommandLine;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name = "iotsimulator")
@Log
public class Main implements Runnable {

    @CommandLine.Option(names = {"-n", "--numberOfSimulators"}, required = true)
    private int numberOfSimulators;

    @CommandLine.Option(names = {"-h", "--host"}, required = true)
    private String host;

    @CommandLine.Option(names = {"-p", "--port"}, required = true)
    private int port;

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    @Override
    public void run() {
        log.info(String.format("Trying to start %d simulator to call %s:%d", numberOfSimulators, host, port));

        var executor = Executors.newScheduledThreadPool(Math.max(numberOfSimulators / 4, 1));

        var random = new Random();

        var client = new Client(host, port);

        for (var i = 0; i < numberOfSimulators; i++) {
            var id = UUID.randomUUID().toString();

            log.info(String.format("Simulator %s created", id));

            var temperatureGenerator = new TemperatureGenerator(random.nextInt(5) + 5);

            executor.scheduleAtFixedRate(new IOTSimulator(id, client, temperatureGenerator), random.nextInt(numberOfSimulators), 1, TimeUnit.SECONDS);
        }
    }
}