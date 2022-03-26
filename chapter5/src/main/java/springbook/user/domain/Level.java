package springbook.user.domain;

import java.util.Arrays;

public enum Level {

    BASIC(1), SILVER(2), GOLD3(3);

    private int value;

    Level(int value) {
        this.value = value;
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
