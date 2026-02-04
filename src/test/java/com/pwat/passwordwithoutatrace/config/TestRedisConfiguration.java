package com.pwat.passwordwithoutatrace.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class TestRedisConfiguration {

    private final RedisServer redisServer;

    public TestRedisConfiguration() {
        try {
            this.redisServer = new RedisServer(6370);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Redis server instance", e);
        }
    }

    @PostConstruct
    public void postConstruct() {
        try {
            redisServer.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start Redis server", e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            redisServer.stop();
        } catch (IOException e) {
            throw new RuntimeException("Failed to stop Redis server", e);
        }
    }
}
