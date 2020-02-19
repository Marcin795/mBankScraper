package util;

import model.Credentials;
import mbank.service.LoginInterface;

public class Program {

    final LoginInterface loginInterface;

    public Program(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
    }

    public void run(Credentials credentials) {
        var bankAccess = loginInterface.logIn(credentials);
        CommandLineInterface.printAccounts(bankAccess.getAccounts());
    }

}
