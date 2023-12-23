package com.halcyon.keepfit.model;

import lombok.Getter;

@Getter
public enum Complexity {
    EASY("easy", 100),
    MEDIUM("medium", 200),
    HARD("hard", 300);

    private final String complexity;
    private final int experience;

    Complexity(String complexity, int experience) {
        this.complexity = complexity;
        this.experience = experience;
    }
}
