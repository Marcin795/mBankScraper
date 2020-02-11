package mbank.util;

import mbank.model.Credentials;
import mbank.service.ApiProvider;
import mbank.service.BankAccess;

public class Program {

    public static void run(String[] args) {
        var provider = new ApiProvider();
        Credentials credentials = parseArgsToCredentials(args);
        var account = provider.logIn(credentials);
        printAccounts(account);
    }

    private static Credentials parseArgsToCredentials(String[] args) {
        return new Credentials(args[0], args[1]);
    }

    private static void printAccounts(BankAccess account) {
        var accounts = account.getAccounts();
        CommandLineInterface.printAccounts(accounts);
    }

}
