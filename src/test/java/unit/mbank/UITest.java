package unit.mbank;

import org.junit.jupiter.api.Test;
import mbank.model.response.Account;
import util.UI;

import java.math.BigDecimal;
import java.util.Collections;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UITest {

    @Test
    void noOutputForEmptyAccountsList() {
        var actual = new StringBuilder();
        var cli = new UI(actual::append);
        cli.printAccounts(Collections.emptyList());
        assertTrue(actual.toString().isBlank());
    }

    @Test
    void printAccountDetails() {
        var actual = new StringBuilder();
        var cli = new UI(actual::append);
        var account = new Account("TheAccountName",
                "11 1234 5678 9012 3456 7890 1234 5678",
                BigDecimal.valueOf(2137),
                "EU$");
        cli.printAccounts(Collections.singletonList(account));
        assertContains(actual.toString(), "TheAccountName", "11 1234 5678 9012 3456 7890 1234 5678", "2137 EU$");
    }

    private static void assertContains(String actual, String... expected) {
        stream(expected).forEach(e -> assertTrue(actual.contains(e)));
    }

}
