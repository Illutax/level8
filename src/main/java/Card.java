import domainvalue.Suite;
import domainvalue.Value;

import static org.valid4j.Assertive.require;

public class Card implements Comparable<Card> {
    public static final Card JOKER = new Card(Suite.OTHER, "JOKER");
    public static final Card SKIP = new Card(Suite.OTHER, "SKIP");

    private final Value value;
    private final Suite suite;
    private final String otherName;

    /**
     * Creates a card of a suite and the numeral (NOT ORDINAL!)
     * @param suite - red, green, blue, yellow, orange, purple
     * @param value - the values from 1 to 15
     */
    public Card(final Suite suite, final int value) {
        this(suite, Value.valueOf(value));
    }

    public Card(final Suite suite, final Value value) {
        require(!suite.equals(Suite.OTHER) && value != null, "Value can't be null, when suite is anything other than \"OTHER\"");

        this.value = value;
        this.suite = suite;
        this.otherName = null;
    }

    public Card(final Suite suite, final String otherName) {
        require(suite.equals(Suite.OTHER) && otherName != null && otherName.length() > 0, "Name has to be set, when suite is \"OTHER\"");

        this.value = null;
        this.suite = suite;
        this.otherName = otherName;
    }

    public int valueAsInt() {
        return value == null ? 16 : value.value(); // hack
    }

    public Value value() {
        return value;
    }

    public Suite suite() {
        return suite;
    }

    @Override
    public String toString() {
        final var name = otherName != null ? otherName : suite + " " + value;
        var color = "";
        switch (suite) {
            case RED -> color = Color.ANSI_RED;
            case BLUE -> color = Color.ANSI_BLUE;
            case GREEN -> color = Color.ANSI_GREEN;
            case ORANGE -> color = Color.ANSI_ORANGE;
            case YELLOW -> color = Color.ANSI_YELLOW;
            case PURPLE -> color = Color.ANSI_PURPLE;
            default -> color = Color.ANSI_CYAN;
        }
        return '[' + color + name + Color.ANSI_RESET + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (value != card.value) return false;
        if (suite != card.suite) return false;
        return otherName != null ? otherName.equals(card.otherName) : card.otherName == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (suite != null ? suite.hashCode() : 0);
        result = 31 * result + (otherName != null ? otherName.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Card other) {
        final var thisIntValue = computeIntValue();
        final var otherIntValue = other.computeIntValue();
        return Integer.compare(thisIntValue, otherIntValue);
    }

    private int computeIntValue() {
        var intValue = this.suite.ordinal() * 15;
        if (Suite.OTHER == this.suite) {
            return intValue + (otherName.equals("JOKER") ? 1 : 2);
        }
        return intValue + valueAsInt();
    }
}
