package domainvalue;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.valid4j.Assertive.require;

public enum Value {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ELEVEN(11),
    TWELVE(12),
    THIRTEEN(13),
    FOURTEEN(14),
    FIFTEEN(15);

    private final int value;

    Value(final int value) {
        this.value = value;
    }


    public int value() {
        return value;
    }

    public static Value valueOf(final int value) {
        require(0 < value && value <= Value.values().length, String.format("Value should be in bound: (%d;%d]",0, Value.values().length));

        return Value.values()[value];
    }

    public static boolean isValid(Integer value) {
        return Arrays.stream(values())
                .map(Value::value)
                .anyMatch(value::equals);
    }

    public static String commaSeparatedValues() {
        return Arrays.stream(values())
                .map(Value::toString)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
