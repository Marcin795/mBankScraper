package mbank;

import mbank.exceptions.InvalidCredentials;
import mbank.model.Credentials;
import mbank.payload.response.LoginResponse;
import mbank.service.BankAccess;
import mbank.service.BankInterfaceWrapper;
import mbank.service.Requests;
import mbank.util.Http;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class BankInterfaceWrapperTests {

    @Test
    void requestsCallOderTest() {
        var requests = mock(Requests.class);
        var credentials = new Credentials("u", "p");

        stubRequests(requests, credentials);

        var bankInterfaceWrapper = new BankInterfaceWrapper(requests);
        var bankAccess = bankInterfaceWrapper.logIn(credentials);

        Assertions.assertEquals(new BankAccess(requests), bankAccess);

        InOrder order = inOrder(requests);
        order.verify(requests).getJsonLogin(credentials);
        order.verify(requests).fetchVerificationToken();
        order.verify(requests).fetchAuthorizationId();
        order.verify(requests).fetchTranId("verificationToken", "authorizationId");
        order.verify(requests).getStatus("tranId");
        order.verify(requests).execute("verificationToken");
        order.verify(requests).finalizeAuthorization("authorizationId");
    }

    static void stubRequests(Requests requests, Credentials credentials) {
        when(requests.getJsonLogin(credentials)).thenReturn(new LoginResponse(true, ""));
        when(requests.fetchVerificationToken()).thenReturn("verificationToken");
        when(requests.fetchAuthorizationId()).thenReturn("authorizationId");
        when(requests.fetchTranId("verificationToken", "authorizationId")).thenReturn("tranId");
        when(requests.getStatus("tranId")).thenReturn("Authorized");
        when(requests.isLoggedIn()).thenReturn(true);
    }

    @Test
    void checkLoginInvalidCredentials() {
        var http = new Http();
        var requests = new Requests(http);
        var bankInterfaceWrapper = new BankInterfaceWrapper(requests);
        var credentials = new Credentials("AdamMałysz", "JestemR4jdowcem");
        Assertions.assertThrows(InvalidCredentials.class, () -> bankInterfaceWrapper.logIn(credentials));
    }

}
