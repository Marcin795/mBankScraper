package model;

import exceptions.InvalidInput;
import util.CommandLineInterface;

public class Credentials {

    public final String username;
    public final String password;

    public Credentials(String... args) {
        checkArgumentCount(args);
        username = args[0];
        password = args[1];
    }

    private static void checkArgumentCount(String... args) {
        if (args.length != 2) {
            CommandLineInterface.argumentsPrompt();
            throw new InvalidInput();
        }
    }

}
