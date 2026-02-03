package com.pwat.passwordwithoutatrace.controller;

import com.pwat.passwordwithoutatrace.model.PasswordEntry;
import com.pwat.passwordwithoutatrace.service.PasswordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passwords")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping
    public String createPassword(@RequestBody CreatePasswordRequest request) {
        return passwordService.createPassword(request.getPassword(), request.getViews(), request.getExpirationTime());
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
}
