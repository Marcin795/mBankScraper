import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.InvalidInput;
import mbank.exceptions.LoginFailed;
import mbank.model.Credentials;
import mbank.service.MBankAccount;
import mbank.service.MBankProvider;

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
        try {
            var mBankProvider = new MBankProvider();
            var account = mBankProvider.logIn(credentials.username, credentials.password);
            printAccounts(account);
        } catch (InvalidCredentials | LoginFailed e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printAccounts(MBankAccount account) {
        var accountsList = account.getAccounts();
        accountsList.accountTypesLists.currentAccounts.forEach(accountData -> {
            System.out.println("Account name: " + accountData.accountName);
            System.out.println("Account number: " + accountData.accountNumber);
            System.out.println("Account balance: " + accountData.balance + " " + accountData.currency);
        });
    }

}
