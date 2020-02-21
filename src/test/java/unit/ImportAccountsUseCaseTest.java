package unit;

import app.ImportAccountsUseCase;
import model.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import unit.mbank.service.BankAccess;
import unit.mbank.service.LoginCommand;
import util.CommandLineInterface;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class ImportAccountsUseCaseTest {

    @Mock
    private LoginCommand loginCommand;

    @Mock
    private BankAccess bankAccess;

    @Mock
    private CommandLineInterface cli;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    void importAccountsUseCasePrintsCorrectAccountDataReceivedFromTheBankAfterLogin() {
        var credentials = new Credentials("jan", "paweł");
        when(bankAccess.getAccounts()).thenReturn(Collections.emptyList());
        when(loginCommand.logIn(credentials)).thenReturn(bankAccess);
        var importAccountsUseCase = new ImportAccountsUseCase(loginCommand, cli);
        importAccountsUseCase.run(credentials);
        verify(cli, times(1)).printAccounts(ArgumentMatchers.anyList());
    }

}
