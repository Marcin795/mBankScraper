package mbank;

import mbank.model.AccountTypesLists;
import mbank.model.Credentials;
import mbank.model.CurrentAccount;
import mbank.payload.request.InitPrepareRequest;
import mbank.payload.request.LoginRequest;
import mbank.payload.request.StatusRequest;
import mbank.payload.response.*;
import mbank.service.Requests;
import mbank.util.Http;
import okhttp3.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestsTests {

    Http http = mock(Http.class);
    Requests requests = new Requests(http);

    @Test
    void getJsonLoginTest() {
        var credentials = new Credentials("correctUsername", "correctPassword");
        var loginResponse = new LoginResponse(true, "");
        when(http.post(ArgumentMatchers.eq("/pl/LoginMain/Account/JsonLogin"), ArgumentMatchers.eq(LoginResponse.class), ArgumentMatchers.isA(LoginRequest.class)))
                .thenReturn(new Response<>(loginResponse));
        Assertions.assertEquals(loginResponse, requests.getJsonLogin(credentials));
    }

    @Test
    void fetchVerificationTokenTest() {
        when(http.get("/api/app/setup/data", SetupDataResponse.class))
                .thenReturn(new Response<>(new SetupDataResponse("verificationToken")));
        Assertions.assertEquals("verificationToken", requests.fetchVerificationToken());
    }

    @Test
    void fetchAuthorizationIdTest() {
        when(http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class))
                .thenReturn(new Response<>(new ScaDataResponse("verificationToken")));
        Assertions.assertEquals("verificationToken", requests.fetchAuthorizationId());
    }

    @Test
    void fetchTranIdTest() {
        when(
                http.post(ArgumentMatchers.eq("/api/auth/initprepare"),
                        ArgumentMatchers.eq(InitPrepareResponse.class),
                        ArgumentMatchers.isA(InitPrepareRequest.class),
                        ArgumentMatchers.isA(Headers.class))
        ).thenReturn(new Response<>(new InitPrepareResponse("tranId")));
        Assertions.assertEquals("tranId", requests.fetchTranId("verificationToken", "authorizationId"));
    }

    @Test
    void getStatusTest() {
        when(http.post(ArgumentMatchers.eq("/api/auth/status"),
                ArgumentMatchers.eq(StatusResponse.class),
                ArgumentMatchers.isA(StatusRequest.class))
        ).thenReturn(new Response<>(new StatusResponse("Authorized")));
    }

    @Test
    void createSinleEntryHeadersTests() {
        var headers = Requests.createSinleEntryHeaders("key", "value");
        Assertions.assertEquals("value", headers.get("key"));
    }

    @Test
    void isLoggedInTest() {
        when(http.get(ArgumentMatchers.startsWith("/api/chat/init?_="))).thenReturn(new ResponseWithoutBody(200));
        Assertions.assertTrue(requests.isLoggedIn());
    }

    @Test
    void isLoggedInFailedTest() {
        when(http.get(ArgumentMatchers.startsWith("/api/chat/init?_="))).thenReturn(new ResponseWithoutBody(401));
        Assertions.assertFalse(requests.isLoggedIn());
    }

    @Test
    void getAccountListTest() {
        var accounts = new ArrayList<CurrentAccount>();
        accounts.add(new CurrentAccount("accountName", "accountNumber", BigDecimal.valueOf(1337), "EUD"));
        var response = new AccountsListResponse(new AccountTypesLists(accounts));
        when(http.post("/pl/Accounts/Accounts/List", AccountsListResponse.class))
                .thenReturn(new Response<>(response));
        Assertions.assertEquals(response, requests.getAccountList());
    }





}
