package com.test.demo.common.infrastructure;

import com.test.demo.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }
}
