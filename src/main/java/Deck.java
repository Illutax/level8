import domainvalue.Suite;
import domainvalue.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class Deck {
    static final List<Card> ALL_CARDS;

    static {
        ALL_CARDS = new ArrayList<>();
        for (var suite : Suite.colors()) {
            for (var value : Value.values()) {
                ALL_CARDS.add(new Card(suite, value));
            }
        }

        for (int i = 5; i > 0; --i) {
            ALL_CARDS.add(Card.JOKER);
        }

        for (int i = 3; i > 0; --i) {
            ALL_CARDS.add(Card.SKIP);
        }

        requireCorrectSize(ALL_CARDS);
    }

    private final LinkedList<Card> cards;
    static Random rnd = new Random(0);

    public Deck() {
        var unshuffled = new ArrayList<Card>(ALL_CARDS);
        cards = new LinkedList<>();
        while (!unshuffled.isEmpty()) {
            cards.addFirst(unshuffled.remove(rnd.nextInt(unshuffled.size())));
        }

        requireCorrectSize(cards);
    }

    private static void requireCorrectSize(final List<Card> cards) {
        require(cards, notNullValue());
        require(cards.size() == 15 * 6 + 5 + 3, "A deck should contain 98 cards");
    }

    public Card draw() {
        require(!cards.isEmpty(), "Cards should not be empty");
        return cards.pollFirst();
    }

    void sort() {
        cards.sort(Card::compareTo);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < cards.size(); ++i) {
            Card card = cards.get(i);
            sb.append(card);
            if ((i+1)%5 == 0) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    List<Card> cards() {
        return cards;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
