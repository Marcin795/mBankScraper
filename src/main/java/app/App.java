package app;

import model.Credentials;
import unit.mbank.service.LoginCommand;
import unit.mbank.service.Requests;
import util.CommandLineInterface;
import util.Delays;
import util.Http;

class App {

    public static void main(String[] args) {
        var credentials = new Credentials(args);
        var http = new Http();
        var requests = new Requests(http, "https://online.mbank.pl");
        var delays = new Delays();
        var loginInterface = new LoginCommand(requests, delays);
        var cli = new CommandLineInterface();
        var importAccountsUseCase = new ImportAccountsUseCase(loginInterface, cli);
        importAccountsUseCase.run(credentials);
    }

}
