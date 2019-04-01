package com.serdarormanli.sepet.collector.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@RequiredArgsConstructor
@Slf4j
public class TemperateGrpcServer implements CommandLineRunner {

    private final ServerBuilder serverBuilder;
    private final BindableService[] grpcServices;

    private Server server;

    @PostConstruct
    void addServices() {
        for (var service : grpcServices) {
            serverBuilder.addService(service);
        }
    }

    @PreDestroy
    private void tearDown() {
        log.info("Shutting down gRPC server");

        server.shutdown();

        log.info("gRPC server shut down");
    }

    @Override
    public void run(String... args) throws Exception {
        server = serverBuilder.build().start();

        log.info("gRPC server started, listening on {}", server.getPort());

        startDaemonAwaitThread();
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                log.error("gRPC server stopped", e);
            }
        });

        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}