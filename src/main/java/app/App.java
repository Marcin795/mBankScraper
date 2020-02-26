package app;

import model.Credentials;
import mbank.service.LoginCommand;
import mbank.service.Requests;
import util.UI;
import util.Delays;
import util.Http;

public class App {

    public static void main(String... args) {
        var requests = new Requests(new Http());
        var cli = new UI(System.out::println);
        var loginCommand = new LoginCommand(requests, new Delays(), cli);
        var importAccountsUseCase = new ImportAccountsUseCase(loginCommand, cli);
        var credentials = new Credentials(args);
        importAccountsUseCase.run(credentials);
    }

}
