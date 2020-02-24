package unit.mbank;

import org.junit.jupiter.api.Test;
import unit.mbank.model.response.Account;
import util.CommandLine;

import java.math.BigDecimal;
import java.util.Collections;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandLineTest {

    private String actual = "";
    private final CommandLine cli = new CommandLine(line -> actual += line);

    @Test
    void noOutputForEmptyAccountsList() {
        cli.printAccounts(Collections.emptyList());
        assertTrue(actual.isBlank());
    }

    @Test
    void printAccountDetails() {
        var account = new Account("TheAccountName",
                "11 1234 5678 9012 3456 7890 1234 5678",
                BigDecimal.valueOf(2137),
                "EU$");
        cli.printAccounts(Collections.singletonList(account));
        assertContains(actual, "TheAccountName", "11 1234 5678 9012 3456 7890 1234 5678", "2137 EU$");
    }

    private static void assertContains(String actual, String... expected) {
        stream(expected).forEach(e -> assertTrue(actual.contains(e)));
    }

}
