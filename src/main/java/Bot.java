import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Bot extends Player {

    private static final Random rnd = new Random(0);

    private boolean openHand = false;

    public Bot(String name) {
        super(name, true);
    }

    @SneakyThrows(InterruptedException.class)
    public void doBotTurn() {
        doRobotSounds();

        //thinking
        Thread.sleep(500);

        final var drawnCard = GameState.CURRENT_DECK.draw();
        if (openHand) {
            talk("Ooh, got " + drawnCard);
        }
        addCard(drawnCard);

        //thinking
        Thread.sleep(1500);

        final var discartedCard = hand.discard(rnd.nextInt(hand.amountOfCards()));
        if (openHand)
        {
            talk("Bye bye " + discartedCard);
        }
    }

    public void letMePeek() {
        openHand = true;
    }

    private void doRobotSounds() {
        talk("Beep-boop-beep");
    }

    private void talk(String dialog) {
        System.out.print(printableName());
        System.out.print(" :");
        System.out.println(dialog);
    }

    @Override
    public String printableName() {
        return "Bot - " + name;
    }
}
