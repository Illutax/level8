package domainvalue;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Suite {
    RED, GREEN, BLUE, ORANGE, YELLOW, PURPLE, OTHER;

    public static Suite[] colors() {
        return new Suite[]{RED, GREEN, BLUE, ORANGE, YELLOW, PURPLE};
    }

    public static boolean isValid(String suite) {
        return Arrays.stream(colors())
                .map(Suite::toString)
                .anyMatch(suite::equalsIgnoreCase);
    }

    public static String commaSeparatedValues() {
        return Arrays.stream(colors())
                .map(Suite::toString)
                .collect(Collectors.joining(", "));
    }
}
