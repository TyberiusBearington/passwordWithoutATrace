package com.pwat.passwordwithoutatrace.model;

import java.io.Serializable;

public class PasswordEntry implements Serializable {
    private String password;
    private int remainingViews;

    public PasswordEntry() {}

    public PasswordEntry(String password, int remainingViews) {
        this.password = password;
        this.remainingViews = remainingViews;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRemainingViews() {
        return remainingViews;
    }

    public void setRemainingViews(int remainingViews) {
        this.remainingViews = remainingViews;
    }
    
    public void decrementViews() {
        this.remainingViews--;
    }
}
