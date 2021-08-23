import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class App implements CommandLineRunner {

    private GameState gs;

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(App.class, args);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(final String... args) throws Exception {
        var arguments = parseArgs(args);
        final var cheatsEnabled = arguments.contains("--cheats");

        if (cheatsEnabled) {
            log.warn("CHEATS ENABLED!");
        }

        gs = new GameState(cheatsEnabled);
        gs.startGameLoop();
    }

    private List<String> parseArgs(String[] args) {
        return Arrays.asList(args);
    }

}
