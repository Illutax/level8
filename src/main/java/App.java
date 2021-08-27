import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class App implements CommandLineRunner {

    private final static String NAME_REGEX = "\\w{1,255}";

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(App.class, args);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(final String... args) throws Exception
    {
        var arguments = parseArgs(args);
        final var cheatsEnabled = arguments.contains("--cheats");

        Optional<String> playerName = Optional.empty();
        for (final String argument : arguments)
        {
            if (!argument.startsWith("--"))
            {
                playerName = Optional.of(argument);
            }
        }

        if (cheatsEnabled)
        {
            log.warn("CHEATS ENABLED!");
        }

        final var initialNameMatches = playerName.isPresent() && playerName.get().matches(NAME_REGEX);
        if (!initialNameMatches)
        {
            if (playerName.isPresent()) {
                printSyntaxError();
            }
            System.out.print("What is your name? ");
            var scanner = new Scanner(System.in);
            while (true)
            {
                final var input = scanner.nextLine();
                if (input.matches(NAME_REGEX))
                {
                    playerName = Optional.of(input);
                    break;
                }
                else
                {
                    printSyntaxError();
                }
            }
        }

        final GameState gs = new GameState(playerName.get(), cheatsEnabled);
        gs.startGameLoop();
    }

    private void printSyntaxError()
    {
        System.out.println("Invalid, should be alphanumeric, not empty and not longer than 255 symbols.");
    }

    private List<String> parseArgs(String[] args) {
        return Arrays.asList(args);
    }

}
