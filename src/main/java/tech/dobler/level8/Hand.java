package tech.dobler.level8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>(11);
    }

    public void addCard(Card card) {
        require(card, notNullValue());

        cards.add(card);
    }

    public Card discard(Card card) {
        require(cards.contains(card), "Can't discard, what you dont have  ¯\\_(ツ)_/¯");

        cards.remove(card);
        return card; //remove?
    }

    /**
     * Removes the card at index i, where 0 <= i <= card.size()
     */
    public Card discard(int cardIndex) {
        require(cards.size() > cardIndex, String.format("Can't discard (%s), what you dont have (>%s)  ¯\\_(ツ)_/¯", cardIndex, cards.size()));

        return cards.remove(cardIndex);
    }

    public void sortBySuiteThenByValue() {
        cards.sort(Card::compareTo);
    }

    public void sortByValueThenBySuite() {
        cards.sort(Comparator.comparingInt(Card::valueAsInt).thenComparing(Card::suite));
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        for (var card : cards) {
            sb.append(card);
        }
        return sb.toString();
    }

    public int amountOfCards() {
        return cards.size();
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int lastCardIndex() {
        return amountOfCards() - 1;
    }
}
