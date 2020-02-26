package app;

import model.Credentials;
import mbank.service.LoginCommand;
import util.UI;

public class ImportAccountsUseCase {

    private final LoginCommand loginCommand;
    private final UI cli;

    public ImportAccountsUseCase(LoginCommand loginCommand, UI cli) {
        this.loginCommand = loginCommand;
        this.cli = cli;
    }

    public void run(Credentials credentials) {
        var bankAccess = loginCommand.logIn(credentials);
        cli.printAccounts(bankAccess.getAccounts());
    }

}
