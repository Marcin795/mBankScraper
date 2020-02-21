package unit.mbank;

import model.Credentials;
import okhttp3.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import unit.mbank.model.response.Account;
import unit.mbank.model.response.AccountTypesLists;
import unit.mbank.model.request.InitPrepareRequestBody;
import unit.mbank.model.request.LoginRequestBody;
import unit.mbank.model.request.StatusRequestBody;
import unit.mbank.model.response.*;
import unit.mbank.service.Requests;
import util.Http;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class RequestsTest {

    private static final String BANK_ADDRESS = "https://online.mbank.pl";

    @Mock
    private Http http;

    private Requests requests;

    @BeforeEach
    void init() {
        initMocks(this);
        requests = new Requests(http, BANK_ADDRESS);
    }

    @Test
    void getJsonLoginReturnsLoginResponseBody() {
        var credentials = new Credentials("correctUsername", "correctPassword");
        var expected = new LoginResponseBody(true, "");
        when(http.post(eq(BANK_ADDRESS + "/pl/LoginMain/Account/JsonLogin"), eq(LoginResponseBody.class), isA(LoginRequestBody.class)))
                .thenReturn(expected);
        var actual = requests.getJsonLogin(credentials);
        assertEquals(expected, actual);
    }

    @Test
    void fetchVerificationTokenReturnsVerificationTokenFromResponseBody() {
        var expected = "verificationToken";
        when(http.get(BANK_ADDRESS + "/api/app/setup/data", SetupDataResponseBody.class))
                .thenReturn(new SetupDataResponseBody(expected));
        var actual = requests.fetchVerificationToken();
        assertEquals(expected, actual);
    }

    @Test
    void fetchAuthorizationIdReturnsIdFromResponseBody() {
        var expected = "authorizationId";
        when(http.post(BANK_ADDRESS + "/pl/Sca/GetScaAuthorizationData", ScaDataResponseBody.class))
                .thenReturn(new ScaDataResponseBody(expected));
        var actual = requests.fetchAuthorizationId();
        assertEquals(expected, actual);
    }

    @Test
    void fetchTranIdReturnsTranIdFromResponseBody() {
        var expected = "tranId";
        when(
                http.post(eq(BANK_ADDRESS + "/api/auth/initprepare"),
                        eq(InitPrepareResponseBody.class),
                        isA(InitPrepareRequestBody.class),
                        isA(Headers.class))
        ).thenReturn(new InitPrepareResponseBody(expected));
        var actual = requests.fetchTranId("verificationToken", "authorizationId");
        assertEquals(expected, actual);
    }

    @Test
    void getStatusReturnsStatusFromResponseBody() {
        var expected = "Authorized";
        when(http.post(eq(BANK_ADDRESS + "/api/auth/status"),
                eq(StatusResponseBody.class),
                isA(StatusRequestBody.class))
        ).thenReturn(new StatusResponseBody(expected));
        var actual = requests.getStatus("tranId");
        assertEquals(expected, actual);
    }

    @Test
    void createSingleEntryHeadersReturnsCorrectHeader() {
        var expected = "value";
        var headers = Requests.createSinleEntryHeaders("key", expected);
        var actual = headers.get("key");
        assertEquals(expected, actual);
    }

    @Test
    void isLoggedInReturnsTrueIfChatReturns200() {
        when(http.get(startsWith(BANK_ADDRESS + "/api/chat/init?_="))).thenReturn(200);
        var condition = requests.isLoggedIn();
        assertTrue(condition);
    }

    @Test
    void isLoggedInReturnsFalseIfChatReturns401() {
        when(http.get(startsWith(BANK_ADDRESS + "/api/chat/init?_="))).thenReturn(401);
        var condition = requests.isLoggedIn();
        assertFalse(condition);
    }

    @Test
    void getAccountListReturnsAccountListFromResponseBody() {
        var account = new Account("TheAccountName",
                "11 1234 5678 9012 3456 7890 1234 5678",
                BigDecimal.valueOf(2137),
                "EU$");
        var accounts = Collections.singletonList(account);
        var expected = new AccountsListResponseBody(new AccountTypesLists(accounts));
        when(http.post(BANK_ADDRESS + "/pl/Accounts/Accounts/List", AccountsListResponseBody.class))
                .thenReturn(expected);
        var actual = requests.getAccountList();
        assertEquals(expected, actual);
    }

}
