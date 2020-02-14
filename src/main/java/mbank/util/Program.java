package mbank.util;

import mbank.model.Credentials;
import mbank.service.BankInterfaceWrapper;
import mbank.service.Requests;

public class Program {

    public static void run(String username, String password) {
        var http = new Http();
        var requests = new Requests(http);
        var bankInterfaceWrapper = new BankInterfaceWrapper(requests);
        var account = bankInterfaceWrapper.logIn(new Credentials(username, password));
        CommandLineInterface.printAccounts(account.getAccounts());
    }

}
