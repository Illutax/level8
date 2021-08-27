import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

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
    GOAL("goal"),
    DROP("drop"),
    SORT("sort"),
    DISPLAY("display");

    private static final Scanner scanner = new Scanner(System.in);
    private final String value;
    private final List<String> args = new ArrayList<>();

    Command(String value) {
        this.value = value;
    }

    public static Command readCommand() {
        String cmd;
        while (true)
        {
            System.out.print("What do you want to do? ");
            final var input = Arrays.asList(scanner.nextLine().toLowerCase().split("\\s+"));
            if (input.isEmpty())
            {
                continue;
            }

            cmd = input.get(0);

            if (isValid(cmd))
            {
                final var command = ofString(cmd);
                command.args.clear(); // enums are not initialized

                if (input.size() > 1) {
                    command.args.addAll(input.subList(1, input.size()));
                }

                return command;
            }

            printHelpText();
        }
    }

    private static Command ofString(final String commandName)
    {
        return allValueMap().get(commandName);
    }

    @UnmodifiableView
    @NotNull
    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }

    private static void printHelpText()
    {
        System.out.println(helpText());
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
