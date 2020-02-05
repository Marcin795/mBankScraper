package mbank;

import mbank.exceptions.InvalidCredentials;
import mbank.service.Provider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class providerTests {

    private static final String LOGIN = "";
    private static final String PASSWORD = "";

    @Test
    void checkLoginCorrectCredentials() {
        var provider = new Provider();
        try {
            Assertions.assertNotNull(provider.logIn(LOGIN, PASSWORD));
        } catch (InvalidCredentials e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void checkLoginInvalidCredentials() {
        var provider = new Provider();
        Assertions.assertThrows(InvalidCredentials.class, () -> provider.logIn("AdamMałysz", "JestemR4jdowcem"));
    }

    @Test
    void checkAccountData() {
        var provider = new Provider();
        var account = provider.logIn(LOGIN, PASSWORD);
        Assertions.assertNotEquals(0, account.getAccounts().accountTypesLists.currentAccounts.size());
    }

}
