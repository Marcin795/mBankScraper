package unit;

import app.ImportAccountsUseCase;
import model.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import mbank.service.BankAccess;
import mbank.service.LoginCommand;
import util.UI;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class ImportAccountsUseCaseTest {

    @Mock
    private LoginCommand loginCommand;

    @Mock
    private BankAccess bankAccess;

    @Mock
    private UI cli;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void printsCorrectAccountData() {
        var credentials = new Credentials("jan", "paweł");
        when(loginCommand.logIn(credentials)).thenReturn(bankAccess);
        when(bankAccess.getAccounts()).thenReturn(Collections.emptyList());
        var importAccountsUseCase = new ImportAccountsUseCase(loginCommand, cli);
        importAccountsUseCase.run(credentials);
        verify(cli, times(1)).printAccounts(anyList());
    }

}
