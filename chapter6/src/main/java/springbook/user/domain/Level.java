package springbook.user.domain;

import java.util.Arrays;

public enum Level {

    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(final int value, final Level next) {
        this.value = value;
        this.next = next;
    }

    public int intValue() {
        return this.value;
    }

    public Level next() {
        return this.next;
    }

    public static Level valueOf(final int value) {
        final Level[] values = values();
        return Arrays.stream(values)
            .filter(level -> level.value == value)
            .findFirst()
            .orElseThrow(AssertionError::new);
    }

}
