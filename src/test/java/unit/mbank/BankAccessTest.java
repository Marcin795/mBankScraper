package unit.mbank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import mbank.model.response.Account;
import mbank.model.response.AccountTypesLists;
import mbank.model.response.AccountsListResponseBody;
import mbank.service.BankAccess;
import mbank.service.Requests;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.*;

class BankAccessTest {

    @Mock
    private Requests requests;

    @InjectMocks
    private BankAccess bankAccess;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void retrievesAndReturnsAccountsFromTheBank() {
        List<Account> expected = new ArrayList<>();
        when(requests.getAccountList()).thenReturn(new AccountsListResponseBody(new AccountTypesLists(expected)));
        var actual = bankAccess.getAccounts();
        assertSame(expected, actual);
    }

}
