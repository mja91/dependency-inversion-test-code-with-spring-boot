package com.test.demo.mock;

import com.test.demo.common.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {

    private final String uuid;

    @Override
    public String random() {
        return null;
    }
}
