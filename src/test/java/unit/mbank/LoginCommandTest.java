package unit.mbank;

import exceptions.LoginFailed;
import model.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unit.mbank.model.response.LoginResponseBody;
import unit.mbank.service.BankAccess;
import unit.mbank.service.LoginCommand;
import unit.mbank.service.Requests;
import util.CommandLine;
import util.Delays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class LoginCommandTest {

    @Mock
    private CommandLine cli;

    @Mock
    private Requests requests;

    @Mock
    private Delays delays;

    @InjectMocks
    private LoginCommand loginCommand;

    @InjectMocks
    private BankAccess expectedBankAccess;

    private final Credentials credentials = new Credentials("user", "pass");

    @BeforeEach
    void init() {
        initMocks(this);
        mockRequests(credentials);
    }

    private void mockRequests(Credentials credentials) {
        when(requests.getJsonLogin(credentials)).thenReturn(new LoginResponseBody(true, ""));
        when(requests.fetchVerificationToken()).thenReturn("verificationToken");
        when(requests.fetchAuthorizationId()).thenReturn("authorizationId");
        when(requests.fetchTranId("verificationToken", "authorizationId")).thenReturn("tranId");
        when(requests.getStatus("tranId")).thenReturn("Authorized");
        when(requests.isLoggedIn()).thenReturn(true);
    }

    @Test
    void requestsCallOrderMatchesRequiredByBank() {
        when(requests.isLoggedIn()).thenReturn(true);
        var actualBankAccess = loginCommand.logIn(credentials);
        assertBankAccessEquals(expectedBankAccess, actualBankAccess);
        var order = inOrder(requests);
        order.verify(requests).getJsonLogin(credentials);
        order.verify(requests).fetchVerificationToken();
        order.verify(requests).fetchAuthorizationId();
        order.verify(requests).fetchTranId("verificationToken", "authorizationId");
        order.verify(requests).getStatus("tranId");
        order.verify(requests).execute("verificationToken");
        order.verify(requests).finalizeAuthorization("authorizationId");
    }

    private static void assertBankAccessEquals(BankAccess expected, BankAccess actual) {
        assertEquals(expected.requests, actual.requests);
    }

    @Test
    void throwsLoginFailed() {
        when(requests.isLoggedIn()).thenReturn(false);
        var loginCommand = this.loginCommand;
        assertThrows(LoginFailed.class, () -> loginCommand.logIn(credentials));
    }

    @Test
    void twoFactorTimeoutsAfter60SecondsAndThrowsLoginFailedException() {
        when(requests.getStatus("tranId")).thenReturn("Prepared");
        assertThrows(LoginFailed.class, () -> loginCommand.logIn(credentials));
        verify(delays, times(60)).waitOneSecond();
    }

}
