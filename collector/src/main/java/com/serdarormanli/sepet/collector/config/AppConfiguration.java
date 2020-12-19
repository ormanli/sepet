package com.serdarormanli.sepet.collector.config;

import com.serdarormanli.sepet.collector.grpc.TemperatureService;
import com.serdarormanli.sepet.collector.service.SenderService;
import com.serdarormanli.sepet.collector.service.SenderServiceImpl;
import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class AppConfiguration {

    @Value("${grpc.port}")
    private int grpcPort;
    @Value("${topic}")
    private String topic;

    @Bean
    public BindableService temperatureService(SenderService senderService) {
        return new TemperatureService(senderService);
    }

    @Bean
    public ServerBuilder serverBuilder() {
        return ServerBuilder.forPort(this.grpcPort);
    }

    @Bean
    public RedisOperations<String, byte[]> redisOperations(RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<String, byte[]>();

        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(RedisSerializer.byteArray());

        return template;
    }

    @Bean
    public Topic topic() {
        return new ChannelTopic(this.topic);
    }

    @Bean
    public SenderService senderService(RedisOperations<String, byte[]> redisOperations, Topic topic) {
        return new SenderServiceImpl(redisOperations, topic);
    }
}
