package domainvalue;

public enum Suite {
    RED, GREEN, BLUE, ORANGE, YELLOW, PURPLE, OTHER;

    public static Suite[] colors() {
        return new Suite[]{RED, GREEN, BLUE, ORANGE, YELLOW, PURPLE};
    }
}
