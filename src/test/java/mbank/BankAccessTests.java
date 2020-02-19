package mbank;

import mbank.model.AccountTypesLists;
import mbank.model.CurrentAccount;
import mbank.model.payload.response.AccountsListResponseBody;
import mbank.service.BankAccess;
import mbank.service.Requests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankAccessTests {

    @Test
    void getAccountsTest() {
        var requests = mock(Requests.class);
        var bankAccess = new BankAccess(requests);
        List<CurrentAccount> accounts = new ArrayList<>();
        accounts.add(new CurrentAccount("accountName", "accountNumber", BigDecimal.valueOf(1337), "EUD"));
        when(requests.getAccountList()).thenReturn(new AccountsListResponseBody(new AccountTypesLists(accounts)));
        Assertions.assertEquals(accounts, bankAccess.getAccounts());
    }

}
