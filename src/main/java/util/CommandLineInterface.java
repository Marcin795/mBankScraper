package util;

import unit.mbank.model.response.Account;

import java.util.List;

public class CommandLineInterface {

    public static void argumentsPrompt() {
        printLines("Pass username and password as arguments");
    }

    public static void twoFactorPrompt() {
        printLines("Waiting for 2FA confirmation...");
    }

    @SuppressWarnings("MethodMayBeStatic")
    public void printAccounts(List<Account> accounts) {
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

}
