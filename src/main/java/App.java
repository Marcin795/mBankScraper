import mbank.exceptions.BankAccountException;
import mbank.exceptions.InvalidCredentialsException;
import mbank.exceptions.LoginFailedException;
import mbank.service.MBankAccount;
import mbank.service.MBankProvider;

public class App {

    public static void main(String[] args) {

        var mBankProvider = new MBankProvider();

        if (args.length != 2) {
            System.out.println("Pass username and password as arguments: ");
            System.out.println("mbankScraper username password");
            return;
        }

        String username = args[0];
        String password = args[1];

        try {
            var account = mBankProvider.login(username, password);
            printAccounts(account);
        } catch (InvalidCredentialsException e) {
            System.out.println("Passed credentials are invalid.");
        } catch (LoginFailedException e) {
            System.out.println("Login failed: " + e.getMessage());
        } catch (BankAccountException e) {
            System.out.println("Bank account connection failed: ");
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
