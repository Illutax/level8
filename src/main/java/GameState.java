import domainvalue.Suite;
import domainvalue.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

@Slf4j
public class GameState {
    public static GameState CURRENT_GAME;
    public static Deck CURRENT_DECK = new Deck();

    private final List<Player> players;
    private final String hexValue = "^((1[0-5]?)|[1-9])$"; // [1-15]
    Random rnd = new Random(0);
    private boolean cardDrawn;
    private int turn;

    private GameState() {
        this(false);
    }

    public GameState(boolean cheats) {
        CURRENT_GAME = this;
        Bot bot = Player.newBot();
        if (cheats) {
            bot.letMePeek();
        }
        players = List.of(bot, Player.newHuman("gary"));
        turn = rnd.nextInt(players.size());
    }

    public void startGameLoop() {
        // do init stuff
        drawInitialCards();
        loop();
    }

    private void drawInitialCards() {
        for (int i = 0; i < 10; i++) {
            for (var player : players) {
                var card = CURRENT_DECK.draw();
                player.addCard(card);
            }
        }
    }

    private void loop() {
        while (true) {
            final var player = players.get(turn);
            talk("It's " + player.printableName() + " turn.");

            if (player.isBot()) {
                var bot = (Bot) player;
                bot.doBotTurn();
            } else {
                Command command = Command.readCommand();
                switch (command) {
                    case HELP -> talk(Command.helpText());
                    case DISPLAY_HAND -> displayHand(player);
                    case DRAW -> tryDrawCard(player);
                    case DROP -> tryDropCard(player, command.getArgs());
                    case SORT -> sortAndDisplayHand(player, command.getArgs());
                }
            }
        }
    }

    private void displayHand(Player player) {
        talk("Your hand is: " + player.hand);
    }

    private void sortAndDisplayHand(Player player, List<String> args) {
        player.hand.sortByValueThenBySuite(); // TODO: add parameter
        displayHand(player);
    }

    private void tryDropCard(Player player, List<String> args) {
        require(args, notNullValue());

        if (args.isEmpty()) {
            talk("You need to pass an argument to the drop command. You can drop specific cards like 'drop RED 15' or 'JOKER'. You can also do it positional 'drop 1' or 'drop first' to drop the first card");
            return;
        }
        if (cardDrawn) {
            Optional<Card> specificCard = tryGetSpecificCard(args);
            if (specificCard.isPresent()) {
                player.discard(specificCard.get());
            } else if (args.size() == 1) {
                Optional<Integer> position = tryParsePosition(args);

                if (position.isEmpty()) {
                    talk("Tried to parse %s as a position. This does not work; it should be a number between 1 and 15 or the words 'first', 'second', 'last'");
                }
            }
        } else {
            talk("First draw a card, then you can drop one card.");
        }
    }

    private Optional<Integer> tryParsePosition(List<String> args) {
        require(args.size() == 1, "There should exactly one argument");

        Optional<Integer> position = Optional.empty();
        final var argument = args.get(0);
        try {
            int value = Integer.parseInt(argument);
            return Optional.of(value);
        } catch (NumberFormatException ignored) {
            // does nothing
        }

        switch (argument) {
            case "first" -> position = Optional.of(1);
            case "second" -> position = Optional.of(2);
            case "last" -> position = Optional.of(Integer.MAX_VALUE);
        }
        return position;
    }

    private Optional<Card> tryGetSpecificCard(List<String> args) {
        if (args.size() == 2) {
            var suiteStr = args.get(0);
            var valueStr = args.get(1);

            boolean suiteIsNumerical = suiteStr.matches(hexValue);
            boolean valueIsNumerical = valueStr.matches(hexValue);

            // did the user swapped them accidentally?
            if (suiteIsNumerical && !valueIsNumerical) {
                log.debug("Swapping valueStr {} and suiteStr {}", valueStr, suiteStr);
                valueStr = args.get(0);
                suiteStr = args.get(1);

                // swap the bools
                var tmp = suiteIsNumerical;
                suiteIsNumerical = valueIsNumerical;
                valueIsNumerical = tmp;
            }

            Suite suite = null;
            if (Suite.isValid(suiteStr)) {
                suite = Suite.valueOf(suiteStr.toUpperCase());
            }

            Value value = null;
            if (valueIsNumerical) {
                require(valueStr.matches(hexValue), "Should never fail");
                final var valueAsInt = Integer.parseInt(valueStr);
                require(Value.isValid(valueAsInt), "Should never fail aswell");

                value = Value.valueOf(valueAsInt);
            }

            if (suite != null && value != null) {
                return Optional.of(new Card(suite, value));
            }

            if (suite == null) {
                talk("Tried to parse " + suiteStr + " as suite. Valid suites are " + Suite.commaSeparatedValues());
            }

            if (value == null) {
                talk("Tried to parse " + valueStr + " as value. Valid values are " + Value.commaSeparatedValues());
            }
        } else if (args.size() == 1) {
            Optional<Card> otherCard = Optional.empty();
            switch (args.get(0).toLowerCase()) {
                case "joker" -> otherCard = Optional.of(Card.JOKER);
                case "skip" -> otherCard = Optional.of(Card.SKIP);
            }
            return otherCard;
        }

        return Optional.empty();
    }

    private void tryDrawCard(final Player player) {
        if (cardDrawn) {
            talk("You can only draw one card per round.");
        } else {
            final var card = CURRENT_DECK.draw();
            player.addCard(card);
        }
    }


    private void advanceTurn() {
        turn = (++turn) % players.size();
    }

    private void talk(final String dialog) {
        System.out.print("GAME: ");
        System.out.println(dialog);
    }
}
