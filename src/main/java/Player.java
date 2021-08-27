import domainvalue.Level;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.valid4j.Assertive.require;

public class Player {
    protected final String name;
    protected final Hand hand;
    private final boolean isBot;
    private Level currentLevel;

    public Player(final String name) {
        this(name, false);
    }

    protected Player(final String name, final boolean isBot) {
        require(name, notNullValue());
        require(name.length() > 0, "Name can't be empty");

        this.name = name;
        this.isBot = isBot;
        this.hand = new Hand();
        this.currentLevel = new Level(1, Level.Difficulty.EASY);
    }

    static Bot newBot() {
        return new Bot(randomName());
    }

    private static String randomName() {
        return "Joe";
    }

    static Player newHuman(final String name) {
        return new Player(name);
    }

    public String getName() {
        return name;
    }

    public boolean isBot() {
        return isBot;
    }

    public void addCard(Card card) {
        hand.addCard(card);
    }

    public Card discard(Card card) {
        return hand.discard(card);
    }

    /**
     * Removes the card at index i, where 0 <= i <= card.size()
     */
    protected Card discard(int cardIndex) {
        return hand.discard(cardIndex);
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        if (isBot) {
            sb.append("[Bot - ")
                    .append(name)
                    .append("] ");
        } else {
            sb.append("[").append(name).append("] ");
        }
        //sb.append(hand);
        return sb.toString();
    }

    public String printableName() {
        return name;
    }

    public boolean hasCard(Card card) {
        return hand.cards()
                .contains(card);
    }

    public Level currentLevel()
    {
        return currentLevel;
    }

}
