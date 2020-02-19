package util;

import exceptions.InvalidInput;
import model.Credentials;
import mbank.model.CurrentAccount;

import java.util.List;

public class CommandLineInterface {

    public static void argumentsPrompt() {
        printLines("Pass username and password as arguments");
    }

    public static void twoFactorPrompt() {
        printLines("Waiting for 2FA confirmation...");
    }

    public static void printAccounts(List<CurrentAccount> accounts) {
        accounts.forEach(accountData -> printLines(
                    "Account name: " + accountData.accountName,
                    "Account number: " + accountData.accountNumber,
                    "Account balance: " + accountData.balance + " " + accountData.currency));
    }

    private static void printLines(String... lines) {
        for(var line : lines) {
            System.out.println(line);
        }
    }

    public static Credentials parseCredentials(String[] args) {
        if (args.length != 2) {
            argumentsPrompt();
            throw new InvalidInput();
        }
        return new Credentials(args[0], args[1]);
    }
}
