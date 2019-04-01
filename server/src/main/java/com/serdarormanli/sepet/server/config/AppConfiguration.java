package com.serdarormanli.sepet.server.config;

import com.serdarormanli.sepet.server.listener.TemperatureListener;
import com.serdarormanli.sepet.server.repository.ReadingRepository;
import com.serdarormanli.sepet.server.service.ReadingService;
import com.serdarormanli.sepet.server.service.ReadingServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

@Configuration
public class AppConfiguration {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${topic}")
    private String topic;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public Topic topic() {
        return new ChannelTopic(topic);
    }

    @Bean
    public MessageListener temperatureListenerAdapter(ReadingService readingService) {
        return new TemperatureListener(readingService);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory lettuceConnectionFactory, MessageListener temperatureListenerAdapter, Topic topic) {
        var container = new RedisMessageListenerContainer();

        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(temperatureListenerAdapter, topic);

        return container;
    }

    @Bean
    public ReadingService readingService(ReadingRepository readingRepository) {
        return new ReadingServiceImpl(readingRepository);
    }
}