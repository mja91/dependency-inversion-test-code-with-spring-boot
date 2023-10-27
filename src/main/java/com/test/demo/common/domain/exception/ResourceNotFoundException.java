package com.test.demo.common.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String datasource, long id) {
        super(datasource + "에서 ID " + id + "을 찾을 수 없습니다.");
    }

    public ResourceNotFoundException(String datasource, String id) {
        super(datasource + "에서 ID " + id + "을 찾을 수 없습니다.");
    }
}
