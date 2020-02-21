package app;

import model.Credentials;
import unit.mbank.service.LoginCommand;
import util.CommandLineInterface;

public class ImportAccountsUseCase {

    private final LoginCommand loginCommand;
    private final CommandLineInterface cli;

    public ImportAccountsUseCase(LoginCommand loginCommand, CommandLineInterface cli) {
        this.loginCommand = loginCommand;
        this.cli = cli;
    }

    public void run(Credentials credentials) {
        var bankAccess = loginCommand.logIn(credentials);
        cli.printAccounts(bankAccess.getAccounts());
    }

}
