package app;

import model.Credentials;
import mbank.service.LoginCommand;
import mbank.service.Requests;
import util.CommandLine;
import util.Delays;
import util.Http;

public class App {

    public static void main(String... args) {
        var cli = new CommandLine(System.out::println);
        var credentials = new Credentials(args);
        var requests = new Requests(new Http());
        var loginCommand = new LoginCommand(requests, new Delays(), cli);
        var importAccountsUseCase = new ImportAccountsUseCase(loginCommand, cli);
        importAccountsUseCase.run(credentials);
    }

}
