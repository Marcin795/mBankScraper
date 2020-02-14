package mbank.service;

import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.model.Credentials;
import mbank.payload.response.LoginResponse;
import mbank.util.CommandLineInterface;

import java.util.Set;

public class BankInterfaceWrapper {

    private static final String CANCELED = "Canceled";
    private static final String AUTHORIZED = "Authorized";
    private final Requests requests;

    public BankInterfaceWrapper(Requests requests) {
        this.requests = requests;
    }

    public BankAccess logIn(Credentials credentials) {
        var result = initializeLogin(credentials);
        awaitTwoFactorConfirmation(result.tranId);
        return finalizeLogin(result.verificationToken, result.authorizationId);
    }

    private Result initializeLogin(Credentials credentials) {
        var loginResponse = requests.getJsonLogin(credentials);
        checkLoginSuccessful(loginResponse);
        var verificationToken = requests.fetchVerificationToken();
        var authorizationId = requests.fetchAuthorizationId();
        var tranId = requests.fetchTranId(verificationToken, authorizationId);
        return new Result(verificationToken, authorizationId, tranId);
    }

    public static void checkLoginSuccessful(LoginResponse loginResponse) {
        if(!loginResponse.successful && loginResponse.errorMessageTitle.equals("Nieprawidłowy identyfikator lub hasło."))
            throw new InvalidCredentials("Passed credentials are invalid.");
    }

    private void awaitTwoFactorConfirmation(String tranId) {
        CommandLineInterface.twoFactorPrompt();
        String status = requests.getStatus(tranId);
        for(int tries = 0; tries++ < 60 && statusNotSet(status); status = requests.getStatus(tranId))
            waitOneSecond();
        verifyTwoFactorStatus(status);
    }

    private static boolean statusNotSet(String status) {
        return !Set.of(AUTHORIZED, CANCELED).contains(status);
    }

    private static void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static void verifyTwoFactorStatus(String status) {
        switch(status) {
            case AUTHORIZED:
                CommandLineInterface.twoFactorConfirmation();
                break;
            case CANCELED:
                throw new LoginFailed("2FA Canceled");
            default:
                throw new LoginFailed("2FA Timeout");
        }
    }

    private BankAccess finalizeLogin(String verificationToken, String authorizationId) {
        requests.execute(verificationToken);
        requests.finalizeAuthorization(authorizationId);
        if(requests.isLoggedIn())
            return new BankAccess(requests);
        else
            throw new LoginFailed("Something went wrong");
    }

    private static class Result {

        public final String verificationToken;
        public final String authorizationId;
        public final String tranId;

        public Result(String verificationToken, String authorizationId, String tranId) {

            this.verificationToken = verificationToken;
            this.authorizationId = authorizationId;
            this.tranId = tranId;
        }
    }
}
