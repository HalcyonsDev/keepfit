package com.halcyon.keepfit.model;

import lombok.Getter;

@Getter
public enum Rank {
    BEGINNER("beginner", 0, 10000),
    EXPERT("expert", 10000, 50000),
    MASTER("master", 50000, 100000),
    LEGEND("legend", 100000);

    private final String rank;
    private final int start;
    private int end;

    Rank(String rank, int start, int end) {
        this.rank = rank;
        this.start = start;
        this.end = end;
    }

    Rank(String rank, int start) {
        this.rank = rank;
        this.start = start;
    }

}
