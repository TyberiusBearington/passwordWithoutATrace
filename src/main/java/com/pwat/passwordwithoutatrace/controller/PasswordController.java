package com.pwat.passwordwithoutatrace.controller;

import com.pwat.passwordwithoutatrace.model.PasswordEntry;
import com.pwat.passwordwithoutatrace.service.PasswordService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/passwords")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping
    public String createPassword(@RequestBody CreatePasswordRequest request) {
        long expirationInSeconds = request.getExpirationTime();
        if (request.getExpirationUnit() != null) {
            switch (request.getExpirationUnit().toLowerCase()) {
                case "minutes":
                    expirationInSeconds = TimeUnit.MINUTES.toSeconds(request.getExpirationTime());
                    break;
                case "hours":
                    expirationInSeconds = TimeUnit.HOURS.toSeconds(request.getExpirationTime());
                    break;
                case "days":
                    expirationInSeconds = TimeUnit.DAYS.toSeconds(request.getExpirationTime());
                    break;
                case "seconds":
                default:
                    // already in seconds
                    break;
            }
        }
        return passwordService.createPassword(request.getPassword(), request.getViews(), expirationInSeconds);
    }

    @GetMapping("/{id}")
    public PasswordEntry getPassword(@PathVariable String id) {
        return passwordService.getPassword(id);
    }
}

class CreatePasswordRequest {
    private String password;
    private int views;
    private long expirationTime;
    private String expirationUnit;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getExpirationUnit() {
        return expirationUnit;
    }

    public void setExpirationUnit(String expirationUnit) {
        this.expirationUnit = expirationUnit;
    }
}
