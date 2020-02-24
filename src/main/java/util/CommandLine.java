package util;

import unit.mbank.model.response.Account;

import java.util.List;
import java.util.function.Consumer;

public class CommandLine {

    Consumer<String> printer;

    public CommandLine(Consumer<String> printer) {
        this.printer = printer;
    }

    public void twoFactorPrompt() {
        printLines("Waiting for 2FA confirmation...");
    }

    public void printAccounts(List<Account> accounts) {
        accounts.forEach(accountData -> printLines(
                    "Account name: " + accountData.accountName,
                    "Account number: " + accountData.accountNumber,
                    "Account balance: " + accountData.balance + " " + accountData.currency));
    }

    private void printLines(String... lines) {
        for(var line : lines) {
            printer.accept(line);
        }
    }

}
