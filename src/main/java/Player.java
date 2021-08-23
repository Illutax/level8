import static org.hamcrest.CoreMatchers.notNullValue;
import static org.valid4j.Assertive.require;

public class Player {
    private final String name;
    private final boolean isBot;
    private final Hand hand;

    private Player(final String name) {
        this(name, false);
    }

    private Player(final String name, final boolean isBot) {
        require(name, notNullValue());
        require(name.length() > 0, "Name can't be empty");

        this.name = name;
        this.isBot = isBot;
        this.hand = new Hand();
    }

    static Player newBot() {
        return new Player(randomName(), true);
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
}
