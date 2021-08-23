import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Bot extends Player {

    private static final Random rnd = new Random(0);

    private boolean openHand = false;

    public Bot(String name) {
        super(name, true);
    }

    public void doBotTurn() {
        doRobotSounds();

        final var drawnCard = GameState.CURRENT_DECK.draw();
        if (openHand) {
            talk("Ooh, got " + drawnCard);
        }
        addCard(drawnCard);

        final var discartedCard = hand.discard(rnd.nextInt(hand.amountOfCards()));
        talk("Bye bye " + discartedCard);
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
