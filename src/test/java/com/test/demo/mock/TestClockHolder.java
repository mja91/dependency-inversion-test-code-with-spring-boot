package com.test.demo.mock;

import com.test.demo.common.service.port.ClockHolder;

public class TestClockHolder implements ClockHolder {

    private final long millis;

    public TestClockHolder(long millis) {
        this.millis = millis;
    }

    @Override
    public long millis() {
        return millis;
    }
}
