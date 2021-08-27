package tech.dobler.level8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import tech.dobler.level8.domainvalue.Suite;
import tech.dobler.level8.domainvalue.Value;

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
    private final String STRING_VALUE_BETWEEN_1_AND_15 = "^((1[0-5]?)|[1-9])$";
    Random rnd = new Random(0);
    private boolean cardDrawn;
    private int turn;

    private GameState(final String playerName) {
        this(playerName, false);
    }

    public GameState(final String playerName, boolean cheats) {
        CURRENT_GAME = this;
        Bot bot = Player.newBot();
        if (cheats) {
            bot.letMePeek();
        }
        players = List.of(bot, Player.newHuman(playerName));
        turn = rnd.nextInt(players.size());
    }

    public void startGameLoop() {
        // do init stuff
        drawInitialHands();
        loop();
    }

    private void drawInitialHands() {
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
            talk("It's " + player.printableName() + "s turn.");

            if (player.isBot()) {
                var bot = (Bot) player;
                bot.doBotTurn();
                advanceTurn();
            } else {
                Command command = Command.readCommand();
                switch (command) {
                    case HELP -> talk(Command.helpText());
                    case DISPLAY -> displayHand(player);
                    case GOAL -> displayCurrentGoal(player);
                    case DRAW -> tryDrawCard(player);
                    case DROP -> tryDropCardAndNextTurn(player, command.getArgs());
                    case SORT -> sortAndDisplayHand(player, command.getArgs());
                }
            }
        }
    }

    private void displayCurrentGoal(final Player player)
    {
        talk("You have to get " + player.currentLevel());
    }

    private void displayHand(Player player)
    {
        talk("Your hand is: " + player.hand);
    }

    private void sortAndDisplayHand(Player player, List<String> args)
    {
        if (args.isEmpty() || args.contains("value"))
        {
            player.hand.sortByValueThenBySuite();
        }
        else if (args.contains("suite"))
        {
            player.hand.sortBySuiteThenByValue();
        } else {
            talk("Unknown sorting: \"" + args.get(0) + "\"");
            return;
        }
        displayHand(player);
    }

    private void tryDropCardAndNextTurn(Player player, List<String> args) {
        require(args, notNullValue());

        if (args.isEmpty()) {
            talk("You need to pass an argument to the drop command. You can drop specific cards like 'drop RED 15' or 'JOKER'. You can also do it positional 'drop 1' or 'drop first' to drop the first card");
            return;
        }

        if (!cardDrawn) {
            talk("First draw a card, then you can drop one card.");
            return;
        }

        // was "BLUE 15" or something similar specified?
        Optional<Card> specificCard = tryGetSpecificCard(args);
        if (specificCard.isPresent()) {
            final var cardToBeDiscarded = specificCard.get();
            tryToDiscardSpecificCardAndAdvanceTurn(player, cardToBeDiscarded);
            return;
        }


        if (args.size() == 1) {
            Optional<Integer> position = tryParsePosition(args);

            if (position.isPresent()) {
                tryToDiscardPositionalCardAndAdvanceTurn(player, position.get());
            } else
            {
                talk("Tried to drop \"" + args.get(0) + "\". This does not work; it should be a number between 1 and " + player.hand.amountOfCards() + " or the words 'first', 'second', 'last'");
                Random tmpRnd = new Random();
                final var randomPlayerCard = player.hand.cards().get(tmpRnd.nextInt(player.hand.amountOfCards()));
                talk("You can also drop cards by saying \"drop " + randomPlayerCard.suite() + " " + randomPlayerCard.valueAsInt() + "\"");
            }
        }
    }

    private void tryToDiscardPositionalCardAndAdvanceTurn(Player player, int cardPositionIndex) {
        // Was it "last"?
        if (cardPositionIndex == Integer.MAX_VALUE)
        {
            final var discardedCard = player.discard(player.hand.lastCardIndex());
            talk("Dropping " + discardedCard);
            advanceTurn();
        }
        else if (cardPositionIndex > player.hand.amountOfCards())
        {
            talk("You don't have as many cards");
        }
        else
        {
            final var discardedCard = player.discard(cardPositionIndex - 1);
            talk("Dropping " + discardedCard);
            advanceTurn();
        }
    }

    private void tryToDiscardSpecificCardAndAdvanceTurn(Player player, Card cardToBeDiscarded) {
        if (player.hasCard(cardToBeDiscarded)) {
            final var discardedCard = player.discard(cardToBeDiscarded);
            talk("Dropping " + discardedCard);
            advanceTurn();
        } else {
            talk("You don't have a " + cardToBeDiscarded);
        }
    }

    /**
     * Returns a value between 1 and 11 or Integer.MAX_VALUE
     */
    private Optional<Integer> tryParsePosition(List<String> args) {
        require(args.size() == 1, "There should exactly one argument");

        Optional<Integer> position = Optional.empty();

        // First try with integers
        final var argument = args.get(0);
        try {
            int value = Integer.parseInt(argument);
            if (value < 1 || value > 11)
            {
                return Optional.empty();
            }
            return Optional.of(value);
        } catch (NumberFormatException ignored) {
            // does nothing
        }

        // Second try with words
        switch (argument) {
            case "first" -> position = Optional.of(1);
            case "second" -> position = Optional.of(2);
            case "last" -> position = Optional.of(Integer.MAX_VALUE);
        }
        return position;
    }

    private Optional<Card> tryGetSpecificCard(List<String> args) {

        // Specific one word cards
        if (args.size() == 1) {
            Optional<Card> otherCard = Optional.empty();
            switch (args.get(0).toLowerCase()) {
                case "joker" -> otherCard = Optional.of(Card.JOKER);
                case "skip" -> otherCard = Optional.of(Card.SKIP);
            }
            return otherCard;
        }

        // Trying to recognize two word card description
        if (args.size() == 2) {
            var suiteStr = args.get(0);
            var valueStr = args.get(1);

            boolean suiteIsNumerical = suiteStr.matches(STRING_VALUE_BETWEEN_1_AND_15);
            boolean valueIsNumerical = valueStr.matches(STRING_VALUE_BETWEEN_1_AND_15);

            // did the user swapped them accidentally?
            if (suiteIsNumerical && !valueIsNumerical) {
                log.debug("Swapping valueStr {} and suiteStr {}", valueStr, suiteStr);
                valueStr = args.get(0);
                suiteStr = args.get(1);

                // also swap the value for valueIsNumerical, because we need it later
                valueIsNumerical = suiteIsNumerical;
            }

            Optional<Suite> suite = tryParseSuite(suiteStr);
            Optional<Value> value = tryParseValue(valueStr, valueIsNumerical);

            if (suite.isPresent() && value.isPresent()) {
                return Optional.of(new Card(suite.get(), value.get()));
            }

            if (suite.isEmpty()) {
                talk("Tried to parse " + suiteStr + " as suite. Valid suites are " + Suite.commaSeparatedValues());
            }

            if (value.isEmpty()) {
                talk("Tried to parse " + valueStr + " as value. Valid values are " + Value.commaSeparatedValues());
            }
        }

        return Optional.empty();
    }

    @NotNull
    private Optional<Suite> tryParseSuite(String suiteStr) {
        return Suite.isValid(suiteStr)
                ? Optional.of(Suite.valueOf(suiteStr.toUpperCase()))
                : Optional.empty();
    }

    @NotNull
    private Optional<Value> tryParseValue(String valueStr, boolean valueIsNumerical) {
        if (valueIsNumerical) {
            require(valueStr.matches(STRING_VALUE_BETWEEN_1_AND_15), "Should never fail");
            final var valueAsInt = Integer.parseInt(valueStr);
            require(Value.isValid(valueAsInt), "Should never fail aswell");

            return Optional.of(Value.valueOf(valueAsInt));
        }

        return Optional.empty();
    }

    private void tryDrawCard(final Player player) {
        if (cardDrawn) {
            talk("You can only draw one card per round.");
        } else
        {
            final var card = CURRENT_DECK.draw();
            player.addCard(card);
            talk("You've drawn " + card);
            cardDrawn = true;
        }
    }

    @SneakyThrows(InterruptedException.class)
    private void advanceTurn() {
        turn = (++turn) % players.size();
//        talk("It's " + players.get(turn) + " turn");
        cardDrawn = false;
        Thread.sleep(500);
    }

    private void talk(final String dialog) {
        System.out.print("GAME: ");
        System.out.println(dialog);
    }
}
