package com.pwat.passwordwithoutatrace.service;

import com.pwat.passwordwithoutatrace.model.PasswordEntry;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordService {

    private final RedisTemplate<String, PasswordEntry> redisTemplate;

    public PasswordService(RedisTemplate<String, PasswordEntry> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createPassword(String password, int views, long expirationTime) {
        String id = UUID.randomUUID().toString();
        PasswordEntry entry = new PasswordEntry(password, views);
        redisTemplate.opsForValue().set(id, entry, expirationTime, TimeUnit.SECONDS);
        return id;
    }

    public PasswordEntry getPassword(String id) {
        return redisTemplate.execute(new SessionCallback<PasswordEntry>() {
            @Override
            public <K, V> PasswordEntry execute(RedisOperations<K, V> operations) throws DataAccessException {
                RedisOperations<String, PasswordEntry> ops = (RedisOperations<String, PasswordEntry>) operations;
                
                ops.watch(id);
                PasswordEntry entry = ops.opsForValue().get(id);
                
                if (entry == null) {
                    ops.unwatch();
                    return null;
                }
                
                entry.decrementViews();
                
                ops.multi();
                if (entry.getRemainingViews() <= 0) {
                    ops.delete(id);
                } else {
                    Long ttl = ops.getExpire(id, TimeUnit.SECONDS);
                    if (ttl != null && ttl > 0) {
                        ops.opsForValue().set(id, entry, ttl, TimeUnit.SECONDS);
                    } else {
                        ops.opsForValue().set(id, entry);
                    }
                }
                
                List<Object> results = ops.exec();
                if (results.isEmpty()) {
                    return PasswordService.this.getPassword(id);
                }
                
                return entry;
            }
        });
    }
}
