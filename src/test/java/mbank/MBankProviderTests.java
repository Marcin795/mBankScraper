package mbank;

import mbank.exceptions.InvalidCredentialsException;
import mbank.service.MBankProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MBankProviderTests {

    private static final String LOGIN = "";
    private static final String PASSWORD = "";

    @Test
    void checkLoginCorrectCretentials() {
        var mBankProvider = new MBankProvider();
        assert mBankProvider.login(LOGIN, PASSWORD) != null;
    }

    @Test
    void checkLoginInvalidCredentials() {
        var mBankProvider = new MBankProvider();
        Assertions.assertThrows(InvalidCredentialsException.class, () -> mBankProvider.login("AdamMałysz", "JestemR4jdowcem"));
    }

    @Test
    void checkAccountData() {
        var mBankProvider = new MBankProvider();
        var account = mBankProvider.login(LOGIN, PASSWORD);
        assert account.getAccounts().getAccountTypesLists().getCurrentAccounts().size() > 0;
    }


}
