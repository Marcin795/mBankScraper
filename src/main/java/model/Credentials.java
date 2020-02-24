package model;

import exceptions.InvalidInput;

public class Credentials {

    public final String username;
    public final String password;

    public Credentials(String... args) {
        if (args.length != 2)
            throw new InvalidInput();
        username = args[0];
        password = args[1];
    }

}
