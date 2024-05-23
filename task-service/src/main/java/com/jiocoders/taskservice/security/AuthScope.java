package com.jiocoders.taskservice.security;

public enum AuthScope {

    ADMIN("13579"),
    USER("24680");

    final String value;

    AuthScope(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }

}
