package generic;

import model.Credentials;
import mbank.model.CurrentAccount;
import mbank.service.BankAccess;
import mbank.service.LoginInterface;
import util.Program;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgramTests {

    @Test
    void runTest() {
        var credentials = new Credentials("jan", "paweł");
        var loginInterface = mock(LoginInterface.class);
        var bankAccess = mock(BankAccess.class);
        List<CurrentAccount> accounts = new ArrayList<>();
        accounts.add(new CurrentAccount("TheAccountName", "11 1234 5678 9012 3456 7890 1234 5678", BigDecimal.valueOf(2137), "EU$"));
        when(bankAccess.getAccounts()).thenReturn(accounts);
        when(loginInterface.logIn(credentials)).thenReturn(bankAccess);
        var program = new Program(loginInterface);
        var outputStream = new ByteArrayOutputStream();
        var sout = System.out;
        System.setOut(new PrintStream(outputStream));
        program.run(credentials);
        String output = outputStream.toString();
        System.out.flush();
        System.setOut(sout);
        Assertions.assertTrue(output.contains("TheAccountName"));
        Assertions.assertTrue(output.contains("11 1234 5678 9012 3456 7890 1234 5678"));
        Assertions.assertTrue(output.contains("2137 EU$"));
    }

}
