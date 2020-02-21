package unit.mbank;

import org.junit.jupiter.api.Test;
import unit.mbank.model.response.Account;
import util.CommandLineInterface;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandLineInterfaceTest {

    private static PrintStream sout;

    @Test
    void printAccountsPrintsAccountNameAndAccountNumberAndBalanceWithCurrency() {
        var outputStream = redirectSystemOut();
        var account = new Account("TheAccountName",
                "11 1234 5678 9012 3456 7890 1234 5678",
                BigDecimal.valueOf(2137),
                "EU$");
        var accounts = Collections.singletonList(account);
        var cli = new CommandLineInterface();
        cli.printAccounts(accounts);
        var output = outputStream.toString();
        restoreSystemOut();
        var accountNameCondition = output.contains("TheAccountName");
        var accountNumberCondition = output.contains("11 1234 5678 9012 3456 7890 1234 5678");
        var balanceAndCurrencyCondition = output.contains("2137 EU$");
        assertTrue(accountNameCondition);
        assertTrue(accountNumberCondition);
        assertTrue(balanceAndCurrencyCondition);
    }

    private static OutputStream redirectSystemOut() {
        var outputStream = new ByteArrayOutputStream();
        sout = System.out;
        System.setOut(new PrintStream(outputStream));
        return outputStream;
    }

    private static void restoreSystemOut() {
        System.out.flush();
        System.setOut(sout);
    }

}
