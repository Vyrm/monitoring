package com.serhii.monitor.dao;

public enum Status {
    OK("OK"), WARNING("WARNING"), CRITICAL("CRITICAL");

    private final String text;

    Status(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
