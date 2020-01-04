package mbank;

import mbank.exceptions.InvalidCredentials;
import mbank.service.MBankProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MBankProviderTests {

    private static final String LOGIN = "";
    private static final String PASSWORD = "";

    @Test
    void checkLoginCorrectCredentials() {
        var mBankProvider = new MBankProvider();
        try {
            Assertions.assertNotNull(mBankProvider.logIn(LOGIN, PASSWORD));
        } catch (InvalidCredentials e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void checkLoginInvalidCredentials() {
        var mBankProvider = new MBankProvider();
        Assertions.assertThrows(InvalidCredentials.class, () -> mBankProvider.logIn("AdamMałysz", "JestemR4jdowcem"));
    }

    @Test
    void checkAccountData() {
        var mBankProvider = new MBankProvider();
        var account = mBankProvider.logIn(LOGIN, PASSWORD);
        Assertions.assertNotEquals(0, account.getAccounts().getAccountTypesLists().getCurrentAccounts().size());
    }

}
