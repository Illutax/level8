import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

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
        gs = new GameState();
        gs.startGameLoop();
    }

}
