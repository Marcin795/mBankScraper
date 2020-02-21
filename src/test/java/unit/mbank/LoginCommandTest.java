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
import util.Delays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class LoginCommandTest {

    private final Credentials credentials = new Credentials("user", "pass");

    @Mock
    private Requests requests;

    @Mock
    private Delays delays;

    @InjectMocks
    private LoginCommand loginCommand;

    @InjectMocks
    private BankAccess expectedBankAccess;


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
    void requestsCallOrderMatchesWhatIsRequiredByBank() {
        when(requests.isLoggedIn()).thenReturn(true);
        var actualBankAccess = loginCommand.logIn(credentials);
        var condition = bankAccessEquals(expectedBankAccess, actualBankAccess);
        assertTrue(condition);
        var order = inOrder(requests);
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
    void logInFailedWithoutClearReasonThrowsLoginFailedException() {
        when(requests.isLoggedIn()).thenReturn(false);
        var loginInterface = loginCommand;
        assertThrows(LoginFailed.class, () -> loginInterface.logIn(credentials));
    }

    @Test
    void twoFactorCanceledThrowsLoginFailedException() {
        when(requests.getStatus("tranId")).thenReturn("Canceled");
        assertThrows(LoginFailed.class, () -> loginCommand.logIn(credentials));
    }

    @Test
    void twoFactorTimeoutThrowsLoginFailedException() {
        when(requests.getStatus("tranId")).thenReturn("Prepared");
        assertThrows(LoginFailed.class, () -> loginCommand.logIn(credentials));
    }

    @Test
    void twoFactorWaits60SecondsForTimeout() {
        when(requests.getStatus("tranId")).thenReturn("Prepared");
        try {
            loginCommand.logIn(credentials);
        } catch (LoginFailed e) { /* ignored because it's not being tested here */ }
        verify(delays, times(60)).waitOneSecond();
    }

}
