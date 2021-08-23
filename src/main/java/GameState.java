import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameState {
    public static GameState CURRENT_GAME = new GameState();
    public static Deck CURRENT_DECK = new Deck();

    private final Scanner scanner;
    private final List<Player> players;
    Random rnd = new Random(0);
    private boolean gameStarted;
    private int turn;

    GameState() {
        this.scanner = new Scanner(System.in);
        players = List.of(Player.newHuman("gary"), Player.newBot());
        turn = rnd.nextInt(players.size());
        gameStarted = true;
    }

    public void startGameLoop() {
        // do init stuff
        loop();
    }

    private void loop() {
        while(true)
        {
            final var player = players.get(turn);
            // bot turn
            if (player.isBot())
            {
                drawCardFromDeck(player);
            }
        }
    }

    private void drawCardFromDeck(final Player player) {
        final var card = CURRENT_DECK.draw();
        player.addCard(card);
    }

    private void advanceTurn()
    {
        turn = (++turn)%players.size();
    }
}
