package mbank;

import mbank.model.AccountTypesLists;
import model.Credentials;
import mbank.model.CurrentAccount;
import mbank.model.payload.request.InitPrepareRequestBody;
import mbank.model.payload.request.LoginRequestBody;
import mbank.model.payload.request.StatusRequestBody;
import mbank.model.payload.response.*;
import mbank.service.Requests;
import util.Http;
import model.Response;
import model.ResponseWithoutBody;
import okhttp3.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestsTests {

    final Http http = mock(Http.class);
    final Requests requests = new Requests(http);

    @Test
    void getJsonLoginTest() {
        var credentials = new Credentials("correctUsername", "correctPassword");
        var loginResponse = new LoginResponseBody(true, "");
        when(http.post(eq("/pl/LoginMain/Account/JsonLogin"), eq(LoginResponseBody.class), isA(LoginRequestBody.class)))
                .thenReturn(new Response<>(loginResponse));
        Assertions.assertEquals(loginResponse, requests.getJsonLogin(credentials));
    }

    @Test
    void fetchVerificationTokenTest() {
        when(http.get("/api/app/setup/data", SetupDataResponseBody.class))
                .thenReturn(new Response<>(new SetupDataResponseBody("verificationToken")));
        Assertions.assertEquals("verificationToken", requests.fetchVerificationToken());
    }

    @Test
    void fetchAuthorizationIdTest() {
        when(http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponseBody.class))
                .thenReturn(new Response<>(new ScaDataResponseBody("verificationToken")));
        Assertions.assertEquals("verificationToken", requests.fetchAuthorizationId());
    }

    @Test
    void fetchTranIdTest() {
        when(
                http.post(eq("/api/auth/initprepare"),
                        eq(InitPrepareResponseBody.class),
                        isA(InitPrepareRequestBody.class),
                        isA(Headers.class))
        ).thenReturn(new Response<>(new InitPrepareResponseBody("tranId")));
        Assertions.assertEquals("tranId", requests.fetchTranId("verificationToken", "authorizationId"));
    }

    @Test
    void getStatusTest() {
        when(http.post(eq("/api/auth/status"),
                eq(StatusResponseBody.class),
                isA(StatusRequestBody.class))
        ).thenReturn(new Response<>(new StatusResponseBody("Authorized")));
    }

    @Test
    void createSingleEntryHeadersTests() {
        var headers = Requests.createSinleEntryHeaders("key", "value");
        Assertions.assertEquals("value", headers.get("key"));
    }

    @Test
    void isLoggedInTest() {
        when(http.get(startsWith("/api/chat/init?_="))).thenReturn(new ResponseWithoutBody(200));
        Assertions.assertTrue(requests.isLoggedIn());
    }

    @Test
    void isLoggedInFailedTest() {
        when(http.get(startsWith("/api/chat/init?_="))).thenReturn(new ResponseWithoutBody(401));
        Assertions.assertFalse(requests.isLoggedIn());
    }

    @Test
    void getAccountListTest() {
        var accounts = new ArrayList<CurrentAccount>();
        accounts.add(new CurrentAccount("accountName", "accountNumber", BigDecimal.valueOf(1337), "EUD"));
        var response = new AccountsListResponseBody(new AccountTypesLists(accounts));
        when(http.post("/pl/Accounts/Accounts/List", AccountsListResponseBody.class))
                .thenReturn(new Response<>(response));
        Assertions.assertEquals(response, requests.getAccountList());
    }

}
