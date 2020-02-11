import mbank.exceptions.InvalidInput;
import mbank.model.Credentials;
import mbank.model.CurrentAccount;
import mbank.service.ApiProvider;
import mbank.service.BankAccess;
import mbank.util.CommandLineInterface;

public class App {

    public static void main(String[] args) {
        Credentials credentials = parseArgsToCredentials(args);
        run(credentials);
    }

    private static Credentials parseArgsToCredentials(String[] args) {
        checkArgs(args);
        return new Credentials(args[0], args[1]);
    }

    private static void checkArgs(String[] args) {
        if (args.length != 2) {
            CommandLineInterface.argumentsPrompt();
            throw new InvalidInput();
        }
    }

    private static void run(Credentials credentials) {
        var provider = new ApiProvider();
        var account = provider.logIn(credentials);
        printAccounts(account);
    }

    private static void printAccounts(BankAccess account) {
        var accounts = account.getAccounts();
        CommandLineInterface.printAccounts(accounts);
    }

}
