import domainvalue.Suite;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@Slf4j
class DeckTest {

    @Test
    void draw() {
        var deck = makeDeck();

        var card = deck.draw();

        assertThat(card, is(new Card(Suite.PURPLE, 14)));
        assertThat(card.toString(), is("[\u001B[35mPURPLE 14\u001B[0m]"));
    }

    @Test
    void draw98cardsAndDeckIsEmpty() {
        var deck = makeDeck();

        Card card = null;
        for (int i = 98; i > 0; --i) {
            card = deck.draw();
        }

        assertThat(card, is(new Card(Suite.GREEN, 15)));
        assertThat(deck.isEmpty(), is(true));
    }

    @Test
    void sort() {
        var deck = makeDeck();
        log.info("\"RANDOM\" DECK:\n{}", deck);
        assertThat(deck.cards(), is(not(Deck.ALL_CARDS)));

        deck.sort();
        log.info("Sorted deck:\n{}", deck);

        assertThat(deck.cards(), is(Deck.ALL_CARDS));
    }

    @Test
    void testToString() {
        var deck = makeDeck();

        var string = deck.toString();

        assertThat(string,
                   is("""
                              [[35mPURPLE 14[0m][[0;93mYELLOW 12[0m][[34mBLUE 9[0m][[31mRED 2[0m][[35mPURPLE 4[0m]
                              [[33mORANGE 13[0m][[35mPURPLE 5[0m][[34mBLUE 6[0m][[36mJOKER[0m][[31mRED 14[0m]
                              [[0;93mYELLOW 13[0m][[35mPURPLE 9[0m][[35mPURPLE 11[0m][[31mRED 13[0m][[33mORANGE 3[0m]
                              [[31mRED 1[0m][[33mORANGE 14[0m][[32mGREEN 5[0m][[33mORANGE 2[0m][[35mPURPLE 15[0m]
                              [[0;93mYELLOW 15[0m][[32mGREEN 2[0m][[35mPURPLE 13[0m][[0;93mYELLOW 8[0m][[32mGREEN 8[0m]
                              [[31mRED 11[0m][[32mGREEN 10[0m][[34mBLUE 2[0m][[36mJOKER[0m][[35mPURPLE 2[0m]
                              [[0;93mYELLOW 9[0m][[33mORANGE 5[0m][[32mGREEN 14[0m][[0;93mYELLOW 7[0m][[33mORANGE 8[0m]
                              [[33mORANGE 9[0m][[36mJOKER[0m][[0;93mYELLOW 2[0m][[33mORANGE 15[0m][[34mBLUE 14[0m]
                              [[34mBLUE 11[0m][[36mJOKER[0m][[34mBLUE 5[0m][[0;93mYELLOW 3[0m][[0;93mYELLOW 10[0m]
                              [[34mBLUE 12[0m][[32mGREEN 11[0m][[34mBLUE 15[0m][[0;93mYELLOW 6[0m][[35mPURPLE 3[0m]
                              [[34mBLUE 3[0m][[35mPURPLE 8[0m][[34mBLUE 4[0m][[31mRED 7[0m][[31mRED 10[0m]
                              [[31mRED 4[0m][[35mPURPLE 7[0m][[33mORANGE 1[0m][[35mPURPLE 12[0m][[0;93mYELLOW 11[0m]
                              [[31mRED 3[0m][[33mORANGE 11[0m][[33mORANGE 6[0m][[32mGREEN 12[0m][[32mGREEN 13[0m]
                              [[36mJOKER[0m][[32mGREEN 4[0m][[0;93mYELLOW 4[0m][[36mSKIP[0m][[0;93mYELLOW 1[0m]
                              [[32mGREEN 6[0m][[36mSKIP[0m][[35mPURPLE 1[0m][[32mGREEN 7[0m][[31mRED 6[0m]
                              [[33mORANGE 4[0m][[32mGREEN 9[0m][[34mBLUE 13[0m][[34mBLUE 1[0m][[31mRED 15[0m]
                              [[33mORANGE 10[0m][[0;93mYELLOW 5[0m][[33mORANGE 12[0m][[35mPURPLE 6[0m][[32mGREEN 3[0m]
                              [[31mRED 8[0m][[34mBLUE 10[0m][[31mRED 12[0m][[34mBLUE 7[0m][[0;93mYELLOW 14[0m]
                              [[35mPURPLE 10[0m][[32mGREEN 1[0m][[31mRED 9[0m][[33mORANGE 7[0m][[36mSKIP[0m]
                              [[34mBLUE 8[0m][[31mRED 5[0m][[32mGREEN 15[0m]"""));
    }

    private Deck makeDeck() {
        var prevRnd = Deck.rnd;
        Deck.rnd = new Random(123456789);
        var deck = new Deck();
        Deck.rnd = prevRnd; // is there a better way?

        return deck;
    }
}