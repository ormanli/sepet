package com.serdarormanli.sepet.server.config;

import com.serdarormanli.sepet.grpc.TemperatureReading;
import com.serdarormanli.sepet.server.repository.ReadingRepository;
import com.serdarormanli.sepet.server.service.ReadingService;
import com.serdarormanli.sepet.server.service.ReadingServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Configuration
public class AppConfiguration {

    @Value("${topic}")
    private String topic;

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(this.topic);
    }

    @Bean
    public ReactiveRedisMessageListenerContainer redisContainer(ReactiveRedisConnectionFactory redisConnectionFactory, ChannelTopic topic, ReadingService readingService) {
        var container = new ReactiveRedisMessageListenerContainer(redisConnectionFactory);
        var serializationPair = RedisSerializationContext.SerializationPair.byteArray();

        container.receive(List.of(topic), serializationPair, serializationPair)
                .map(ReactiveSubscription.Message::getMessage)
                .map(this::toTemperatureReading)
                .flatMap(readingService::saveReading)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();

        return container;
    }

    @SneakyThrows
    private TemperatureReading toTemperatureReading(byte[] bytes) {
        return TemperatureReading.parseFrom(bytes);
    }

    @Bean
    public ReadingService readingService(ReadingRepository readingRepository) {
        return new ReadingServiceImpl(readingRepository);
    }
}