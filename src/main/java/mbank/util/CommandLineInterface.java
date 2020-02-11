package mbank.util;

import mbank.model.CurrentAccount;

import java.util.List;

public class CommandLineInterface {

    public static void argumentsPrompt() {
        System.out.println("Pass username and password as arguments");
    }

    public static void twoFactorPrompt() {
        System.out.println("Waiting for 2FA confirmation...");
    }

    public static void printAccounts(List<CurrentAccount> accounts) {
        accounts.forEach(accountData -> {
            System.out.println("Account name: " + accountData.accountName);
            System.out.println("Account number: " + accountData.accountNumber);
            System.out.println("Account balance: " + accountData.balance + " " + accountData.currency);
        });
    }

    public static void twoFactorConfirmation() {
        System.out.println("Login authorized.");
    }
}
