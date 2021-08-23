import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class Hand {
    private final List<Card> cards;

    public Hand()
    {
        cards = new ArrayList<>(11);
    }

    public void addCard(Card card)
    {
        require(card, notNullValue());

        cards.add(card);
    }

    public Card discard(Card card){
        require(cards.contains(card), "Can't discard, what you dont have  ¯\\_(ツ)_/¯");

        cards.remove(card);
        return card; //remove?
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
