package mbank;

import mbank.model.AccountTypesLists;
import mbank.model.CurrentAccount;
import mbank.payload.response.AccountsListResponse;
import mbank.service.Requests;
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
        List<CurrentAccount> accounts = new ArrayList<>();
        accounts.add(new CurrentAccount("accountName", "accountNumber", BigDecimal.valueOf(1337), "EUD"));
        when(requests.getAccountList()).thenReturn(new AccountsListResponse(new AccountTypesLists(accounts)));

    }

}
