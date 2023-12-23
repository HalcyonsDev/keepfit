package com.halcyon.keepfit.model;

import lombok.Getter;

@Getter
public enum Status {
    WAITING("waiting"),
    IN_PROGRESS("in progress"),
    COMPLETED("completed"),
    FAILED("failed");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
