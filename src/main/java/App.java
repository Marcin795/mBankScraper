import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.service.MBankAccount;
import mbank.service.MBankProvider;

public class App {

    public static void main(String[] args) {

        var mBankProvider = new MBankProvider();

        if (args.length != 2) {
            System.out.println("Pass username and password as arguments");
            return;
        }

        String username = args[0];
        String password = args[1];

        try {
            var account = mBankProvider.logIn(username, password);
            printAccounts(account);
        } catch (InvalidCredentials e) {
            System.out.println(e.getMessage());
        } catch (LoginFailed e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void printAccounts(MBankAccount account) {
        var accountsList = account.getAccounts();
        accountsList.getAccountTypesLists().getCurrentAccounts().forEach(accountData -> {
            System.out.println("Account name: " + accountData.getAccountName());
            System.out.println("Account number: " + accountData.getAccountNumber());
            System.out.println("Account balance: " + accountData.getBalance() + " " + accountData.getCurrency());
        });
    }

}
