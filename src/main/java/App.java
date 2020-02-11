import mbank.exceptions.InvalidInput;
import mbank.model.Credentials;
import mbank.service.ApiProvider;
import mbank.service.BankAccess;

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
            System.out.println("Pass username and password as arguments");
            throw new InvalidInput();
        }
    }

    private static void run(Credentials credentials) {
        var provider = new ApiProvider();
        var account = provider.logIn(credentials.username, credentials.password);
        printAccounts(account);
    }

    private static void printAccounts(BankAccess account) {
        var accounts = account.getAccounts();
        accounts.forEach(accountData -> {
            System.out.println("Account name: " + accountData.accountName);
            System.out.println("Account number: " + accountData.accountNumber);
            System.out.println("Account balance: " + accountData.balance + " " + accountData.currency);
        });
    }

}
