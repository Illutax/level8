package tech.dobler.level8;

import tech.dobler.level8.domainvalue.Suite;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.valid4j.errors.RequireViolation;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.valid4j.Assertive.require;

@Slf4j
class HandTest {

    @Test
    void addCard() {
        var hand = new Hand();
        assertThat(hand.amountOfCards(), is(0));
        var blue7 = new Card(Suite.BLUE, 7);

        hand.addCard(blue7);

        assertThat(hand.amountOfCards(), is(1));
        assertThat(hand.cards(), is(List.of(blue7)));
    }

    @Test
    void discard() {
        var hand = makeHand();
        final var red14 = new Card(Suite.RED, 14);

        var card = hand.discard(red14);

        assertThat(card, is(red14));
        assertThat(hand.amountOfCards(), is(9));
        assertThat(hand.cards(), not(contains(card)));
    }

    @Test
    void testToString() {
        var hand = makeHand();

        var string = hand.toString();

        log.info("\"Random\" as dealt: {}", string);
        assertThat(string, is("[[35mPURPLE 14[0m][[0;93mYELLOW 12[0m][[34mBLUE 9[0m][[31mRED 2[0m][[35mPURPLE 4[0m][[33mORANGE 13[0m][[35mPURPLE 5[0m][[34mBLUE 6[0m][[36mJOKER[0m][[31mRED 14[0m]"));
    }

    @Test
    void sortBySuiteThenByValue() {
        var hand = makeHand();

        hand.sortBySuiteThenByValue();

        log.info("sortBySuiteThenByValue: {}", hand);
        assertThat(hand.toString(), is("[\u001B[31mRED 2\u001B[0m][\u001B[31mRED 14\u001B[0m][\u001B[34mBLUE 6\u001B[0m][\u001B[34mBLUE 9\u001B[0m][\u001B[33mORANGE 13\u001B[0m][\u001B[0;93mYELLOW 12\u001B[0m][\u001B[35mPURPLE 4\u001B[0m][\u001B[35mPURPLE 5\u001B[0m][\u001B[35mPURPLE 14\u001B[0m][\u001B[36mJOKER\u001B[0m]"));
    }

    @Test
    void sortByValueThenBySuite() {
        var hand = makeHand();

        hand.sortByValueThenBySuite();

        log.info("sortByValueThenBySuite: {}", hand);
        assertThat(hand.toString(), is("[\u001B[31mRED 2\u001B[0m][\u001B[35mPURPLE 4\u001B[0m][\u001B[35mPURPLE 5\u001B[0m][\u001B[34mBLUE 6\u001B[0m][\u001B[34mBLUE 9\u001B[0m][\u001B[0;93mYELLOW 12\u001B[0m][\u001B[33mORANGE 13\u001B[0m][\u001B[31mRED 14\u001B[0m][\u001B[35mPURPLE 14\u001B[0m][\u001B[36mJOKER\u001B[0m]"));
    }

    @Test
    void emptyHand() {
        var hand = new Hand();

        assertThat(hand.isEmpty(), is(true));
        assertThat(hand.toString(), is(""));
    }

    @Test
    void discardByIndex_outOfRange_resultsInThrows() {
        var hand = makeHand();

        assertThrows(RequireViolation.class, () -> hand.discard(10));
        assertThrows(IndexOutOfBoundsException.class, () -> hand.discard(-1));
        assertThat(hand.discard(9), is(new Card(Suite.RED, 14)));
        assertThat(hand.discard(0), is(new Card(Suite.PURPLE, 14)));
    }

    private Hand makeHand() {
        final var deck = DeckTest.makeDeck();

        final var hand = new Hand();
        for (int i = 10; i > 0; --i) {
            hand.addCard(deck.draw());
        }

        require(hand.amountOfCards() == 10, "After initializing a hand should have 10 cards.");
        return hand;
    }
}