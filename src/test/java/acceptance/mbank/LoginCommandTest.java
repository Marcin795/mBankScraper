package acceptance.mbank;

import exceptions.InvalidCredentials;
import model.Credentials;
import org.junit.jupiter.api.Test;
import unit.mbank.service.LoginCommand;
import unit.mbank.service.Requests;
import util.Delays;
import util.Http;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginCommandTest {

    @Test
    void loginAttemptWithInvalidCredentialsThrowsInvalidCredentialsException() {
        var http = new Http();
        var requests = new Requests(http, "https://online.mbank.pl");
        var delays = new Delays();
        var loginInterface = new LoginCommand(requests, delays);
        var credentials = new Credentials("AdamMałysz", "JestemR4jdowcem");
        assertThrows(InvalidCredentials.class, () -> loginInterface.logIn(credentials));
    }

}
