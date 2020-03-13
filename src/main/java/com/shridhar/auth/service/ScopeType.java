package com.shridhar.auth.service;

public enum ScopeType {

    USER ("openid profile"),
    USERWITHEMAIL ("openid profile email"),
    SERVICE ("openid service");

    private String name;

    ScopeType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
