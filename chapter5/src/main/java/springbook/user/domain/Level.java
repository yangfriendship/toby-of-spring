package springbook.user.domain;

import java.util.Arrays;

public enum Level {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private int value;
    private Level nextLevel;

    Level(int value, Level nextLevel) {
        this.value = value;
        this.nextLevel = nextLevel;
    }

    public int intValue() {
        return this.value;
    }

    public static Level valueOf(final int value) {
        Level[] values = values();
        return Arrays.stream(values)
            .filter(level -> level.value == value)
            .findFirst()
            .orElseThrow(AssertionError::new);
    }

}
