package com.pwat.passwordwithoutatrace.service;

import com.pwat.passwordwithoutatrace.config.TestRedisConfiguration;
import com.pwat.passwordwithoutatrace.model.PasswordEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestRedisConfiguration.class)
public class PasswordServiceIntegrationTest {

    @Autowired
    private PasswordService passwordService;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.port", () -> 6370);
    }

    @Test
    public void testCreateAndRetrievePassword() {
        String password = "secretPassword";
        int views = 2;
        long expiration = 60;

        String id = passwordService.createPassword(password, views, expiration);
        assertNotNull(id);

        // First retrieval
        PasswordEntry entry1 = passwordService.getPassword(id);
        assertNotNull(entry1);
        assertEquals(password, entry1.getPassword());
        assertEquals(1, entry1.getRemainingViews());

        // Second retrieval
        PasswordEntry entry2 = passwordService.getPassword(id);
        assertNotNull(entry2);
        assertEquals(password, entry2.getPassword());
        assertEquals(0, entry2.getRemainingViews());

        // Third retrieval (should be gone)
        PasswordEntry entry3 = passwordService.getPassword(id);
        assertNull(entry3);
    }

    @Test
    public void testPasswordExpiration() {
        String password = "expiredPassword";
        int views = 5;
        long expiration = 1; // 1 second

        String id = passwordService.createPassword(password, views, expiration);
        assertNotNull(id);

        // Wait for expiration
        await().atMost(3, TimeUnit.SECONDS).until(() -> passwordService.getPassword(id) == null);
        
        PasswordEntry entry = passwordService.getPassword(id);
        assertNull(entry);
    }
}
