import domainvalue.Suite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Command {

    HELP("help"),
    DRAW("draw"),
    DROP("drop"),
    SORT("sort"),
    DISPLAY_HAND("display");

    private static final Scanner scanner = new Scanner(System.in);
    private final String value;
    private final List<String> args;

    Command(String value) {
        this.value = value;
        args = new ArrayList<>();
    }

    public static Command readCommand() {
        String cmd;
        List<String> args;
        do {
            System.out.print("What do you want to do? ");
            final var input = scanner.next().toLowerCase();
            final var parts = Arrays.asList(input.split(" "));
            cmd = parts.get(0);
            args = parts.subList(1, parts.size());
        } while (!isValid(cmd) && printHelpText());

        final var command = allValueMap().get(cmd);
        command.args.addAll(args);

        return command;
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }

    // FIXME: HACK
    private static boolean printHelpText() {
        System.out.println(helpText());

        return true;
    }

    private static List<String> allValueStrings() {
        return allValueStream()
                .map(Command::getValue)
                .collect(Collectors.toList());
    }

    private static Map<String, Command> allValueMap() {
        return allValueStream()
                .collect(Collectors.toMap(Command::getValue, command -> command));
    }

    private static Stream<Command> allValueStream() {
        return Arrays.stream(Command.values());
    }

    public static String helpText() {
        return "Available commands: " + Command.commaSeparatedValues();
    }

    private static String commaSeparatedValues() {
        return Arrays.stream(commands())
                .map(Command::toString)
                .collect(Collectors.joining(", "));
    }

    private static Command[] commands() {
        return values();
    }

    private String getValue() {
        return value;
    }


    public static boolean isValid(String command) {
        return allValueMap().containsKey(command);
    }
}
