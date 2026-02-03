package com.pwat.passwordwithoutatrace.service;

import com.pwat.passwordwithoutatrace.model.PasswordEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceTest {

    @Mock
    private RedisTemplate<String, PasswordEntry> redisTemplate;

    @Mock
    private ValueOperations<String, PasswordEntry> valueOperations;

    private PasswordService passwordService;

    @BeforeEach
    public void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        passwordService = new PasswordService(redisTemplate);
    }

    @Test
    public void testCreatePassword() {
        String id = passwordService.createPassword("password", 1, 60);
        assertNotNull(id);
        verify(valueOperations).set(eq(id), any(PasswordEntry.class), eq(60L), eq(TimeUnit.SECONDS));
    }
}
