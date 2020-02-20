package mbank;

import exceptions.InvalidInput;
import model.Credentials;
import mbank.model.CurrentAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import static util.CommandLineInterface.parseCredentials;
import static util.CommandLineInterface.printAccounts;

public class CommandLineInterfaceTests {

    @Test
    void printAccountsTest() {
        var outputStream = new ByteArrayOutputStream();
        var sout = System.out;
        System.setOut(new PrintStream(outputStream));
        var accounts = new ArrayList<CurrentAccount>();
        accounts.add(new CurrentAccount("TheAccountName", "11 1234 5678 9012 3456 7890 1234 5678", BigDecimal.valueOf(2137), "EU$"));
        printAccounts(accounts);
        String output = outputStream.toString();
        System.out.flush();
        System.setOut(sout);
        Assertions.assertTrue(output.contains("TheAccountName"));
        Assertions.assertTrue(output.contains("11 1234 5678 9012 3456 7890 1234 5678"));
        Assertions.assertTrue(output.contains("2137 EU$"));
    }

    @Test
    void parseCredentialsTest() {
        var credentials = parseCredentials(new String[] {"user", "pasło"});
        Assertions.assertTrue(credentialsEquals(new Credentials("user", "pasło"), credentials));
        Assertions.assertThrows(InvalidInput.class, () -> parseCredentials(new String[] {"dupa"}));
        Assertions.assertThrows(InvalidInput.class, () -> parseCredentials(new String[] {"dupa", "gówno", "chuj"}));
    }

    private static boolean credentialsEquals(Credentials a, Credentials b) {
        return a.username.equals(b.username) && a.password.equals(b.password);
    }

}
