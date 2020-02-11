package app;

import mbank.exceptions.InvalidInput;
import mbank.util.CommandLineInterface;
import mbank.util.Program;

public class App {

    public static void main(String[] args) {
        checkArgs(args);
        Program.run(args);
    }

    private static void checkArgs(String[] args) {
        if (args.length != 2) {
            CommandLineInterface.argumentsPrompt();
            throw new InvalidInput();
        }
    }

}
