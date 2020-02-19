package mbank;

import exceptions.InvalidCredentials;
import exceptions.LoginFailed;
import mbank.model.payload.response.LoginResponseBody;
import mbank.service.BankAccess;
import mbank.service.LoginInterface;
import mbank.service.Requests;
import util.Delays;
import util.Http;
import model.Credentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class LoginInterfaceTests {

    Credentials credentials = new Credentials("user", "paslo");
    Requests requests = mockRequests(credentials);
    Delays delays = mock(Delays.class);

    private static Requests mockRequests(Credentials credentials) {
        var requests = mock(Requests.class);
        when(requests.getJsonLogin(credentials)).thenReturn(new LoginResponseBody(true, ""));
        when(requests.fetchVerificationToken()).thenReturn("verificationToken");
        when(requests.fetchAuthorizationId()).thenReturn("authorizationId");
        when(requests.fetchTranId("verificationToken", "authorizationId")).thenReturn("tranId");
        when(requests.getStatus("tranId")).thenReturn("Authorized");
        when(requests.isLoggedIn()).thenReturn(true);
        return requests;
    }

    @Test
    void logInWithRequestsCallOderTest() {
        when(requests.isLoggedIn()).thenReturn(true);
        var loginInterface = new LoginInterface(requests, delays);
        var bankAccess = loginInterface.logIn(credentials);
        Assertions.assertTrue(bankAccessEquals(new BankAccess(requests), bankAccess));
        InOrder order = inOrder(requests);
        order.verify(requests).getJsonLogin(credentials);
        order.verify(requests).fetchVerificationToken();
        order.verify(requests).fetchAuthorizationId();
        order.verify(requests).fetchTranId("verificationToken", "authorizationId");
        order.verify(requests).getStatus("tranId");
        order.verify(requests).execute("verificationToken");
        order.verify(requests).finalizeAuthorization("authorizationId");
    }

    private static boolean bankAccessEquals(BankAccess a, BankAccess b) {
        return a.requests == b.requests;
    }

    @Test
    void logInFailedForWhateverReasonTest() {
        when(requests.isLoggedIn()).thenReturn(false);
        var loginInterface = new LoginInterface(requests, delays);
        Assertions.assertThrows(LoginFailed.class, () -> loginInterface.logIn(credentials));
    }

    @Test
    void loginFailedTwoFactorCanceled() {
        when(requests.getStatus("tranId")).thenReturn("Canceled");
        var loginInterface = new LoginInterface(requests, delays);
        Assertions.assertThrows(LoginFailed.class, () -> loginInterface.logIn(credentials));
    }

    @Test
    void loginFailedTwoFactorTimeout() {
        when(requests.getStatus("tranId")).thenReturn("Prepared");
        var loginInterface = new LoginInterface(requests, delays);
        Assertions.assertThrows(LoginFailed.class, () -> loginInterface.logIn(credentials));
        verify(delays, times(60)).waitOneSecond();
    }

    @Test
    void checkLoginInvalidCredentials() {
        var http = new Http("https://online.mbank.pl");
        var requests = new Requests(http);
        var delays = new Delays();
        var loginInterface = new LoginInterface(requests, delays);
        var credentials = new Credentials("AdamMałysz", "JestemR4jdowcem");
        Assertions.assertThrows(InvalidCredentials.class, () -> loginInterface.logIn(credentials));
    }

}
