package unit.mbank;

import model.Credentials;
import okhttp3.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import mbank.model.request.InitPrepareRequestBody;
import mbank.model.request.LoginRequestBody;
import mbank.model.request.StatusRequestBody;
import mbank.model.response.*;
import mbank.service.Requests;
import util.Http;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RequestsTest {

    @Mock
    private Http http;

    private Requests requests;

    @BeforeEach
    void init() {
        initMocks(this);
        requests = new Requests(http);
    }

    @Test
    void returnsLoginResponse() {
        var credentials = new Credentials("correctUsername", "correctPassword");
        var expected = new LoginResponseBody(true, "");
        when(http.post(
                endsWith("/pl/LoginMain/Account/JsonLogin"),
                eq(LoginResponseBody.class),
                isA(LoginRequestBody.class)
        )).thenReturn(expected);
        var actual = requests.getJsonLogin(credentials);
        assertEquals(expected, actual);
    }

    @Test
    void returnsVerificationToken() {
        var expected = "verificationToken";
        when(http.get(
                endsWith("/api/app/setup/data"),
                eq(SetupDataResponseBody.class)
        )).thenReturn(new SetupDataResponseBody(expected));
        var actual = requests.fetchVerificationToken();
        assertEquals(expected, actual);
    }

    @Test
    void returnsAuthorizationId() {
        var expected = "authorizationId";
        when(http.post(
                endsWith("/pl/Sca/GetScaAuthorizationData"),
                eq(ScaDataResponseBody.class))
        ).thenReturn(new ScaDataResponseBody(expected));
        var actual = requests.fetchAuthorizationId();
        assertEquals(expected, actual);
    }

    @Test
    void returnsTranId() {
        var expected = "tranId";
        when(http.post(
                endsWith("/api/auth/initprepare"),
                eq(InitPrepareResponseBody.class),
                isA(InitPrepareRequestBody.class),
                isA(Headers.class)
        )).thenReturn(new InitPrepareResponseBody(expected));
        var actual = requests.fetchTranId("verificationToken", "authorizationId");
        assertEquals(expected, actual);
    }

    @Test
    void returnsStatus() {
        var expected = "Authorized";
        when(http.post(
                endsWith("/api/auth/status"),
                eq(StatusResponseBody.class),
                isA(StatusRequestBody.class)
        )).thenReturn(new StatusResponseBody(expected));
        var actual = requests.getStatus("tranId");
        assertEquals(expected, actual);
    }

    @Test
    void returnsTrueIfLoggedInSuccessfully() {
        when(http.get(
                contains("/api/chat/init?_=")
        )).thenReturn(200);
        assertTrue(requests.isLoggedIn());
    }

    @Test
    void returnsFalseIfLogInFailed() {
        when(http.get(
                contains("/api/chat/init?_=")
        )).thenReturn(401);
        assertFalse(requests.isLoggedIn());
    }

    @Test
    void returnsAccountList() {
        var account = new Account("TheAccountName",
                "11 1234 5678 9012 3456 7890 1234 5678",
                BigDecimal.valueOf(2137),
                "EU$");
        var accounts = Collections.singletonList(account);
        var expected = new AccountsListResponseBody(new AccountTypesLists(accounts));
        when(http.post(
                endsWith("/pl/Accounts/Accounts/List"),
                eq(AccountsListResponseBody.class)
        )).thenReturn(expected);
        var actual = requests.getAccountList();
        assertEquals(expected, actual);
    }

}
