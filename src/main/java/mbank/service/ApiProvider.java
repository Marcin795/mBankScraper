package mbank.service;

import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.model.Credentials;
import mbank.payload.executionOrder.VerificationTokenAndAuthorizationTokenAndTranId;
import mbank.payload.executionOrder.VerificationTokenAndAuthorizationTokenWithoutTranId;
import mbank.payload.executionOrder.JustEmptyClassToForceProperUsage;
import mbank.payload.response.LoginResponse;
import mbank.util.CommandLineInterface;
import mbank.util.Http;

import java.util.Set;

public class ApiProvider {

    private static final String CANCELED = "Canceled";
    private static final String AUTHORIZED = "Authorized";
    private final Requests requests;

    public ApiProvider() {
        Http http = new Http();
        requests = new Requests(http);
    }

    public BankAccess logIn(Credentials credentials) {
        var verificationTokenAndAuthorizationTokenAndTranId = initializeLogin(credentials);
        var verificationTokenAndAuthorizationTokenWithoutTranId = awaitTwoFactorConfirmation(verificationTokenAndAuthorizationTokenAndTranId);
        return finalizeLogin(verificationTokenAndAuthorizationTokenWithoutTranId);
    }

    private VerificationTokenAndAuthorizationTokenAndTranId initializeLogin(Credentials credentials) {
        var loginResponse = requests.getJsonLogin(credentials);
        checkLoginSuccessful(loginResponse.body);
        var verificationToken = requests.queryForSetupData(loginResponse.body);
        var verificationTokenAndAuthorizationId = requests.queryForScaAuthorizationData(verificationToken);
        return requests.queryForInitPrepare(verificationTokenAndAuthorizationId);
    }

    private static void checkLoginSuccessful(LoginResponse loginResponse) {
        if(!loginResponse.successful && loginResponse.errorMessageTitle.equals("Nieprawidłowy identyfikator lub hasło."))
            throw new InvalidCredentials("Passed credentials are invalid.");
    }

    private VerificationTokenAndAuthorizationTokenWithoutTranId awaitTwoFactorConfirmation(VerificationTokenAndAuthorizationTokenAndTranId verificationTokenAndAuthorizationTokenAndTranId) {
        CommandLineInterface.twoFactorPrompt();
        String status = requests.getStatus(verificationTokenAndAuthorizationTokenAndTranId.tranId);
        for(int tries = 0; tries++ < 60 && statusNotSet(status); status = requests.getStatus(verificationTokenAndAuthorizationTokenAndTranId.tranId))
            waitOneSecond();
        verifyTwoFactorStatus(status);
        return new VerificationTokenAndAuthorizationTokenWithoutTranId(verificationTokenAndAuthorizationTokenAndTranId);
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

    private BankAccess finalizeLogin(VerificationTokenAndAuthorizationTokenWithoutTranId verificationTokenAndAuthorizationTokenWithoutTranId) {
        var authorizationId = requests.execute(verificationTokenAndAuthorizationTokenWithoutTranId);
        JustEmptyClassToForceProperUsage justEmptyClassToForceProperUsage = requests.finalizeAuthorization(authorizationId);
        if(requests.isLoggedIn(justEmptyClassToForceProperUsage).loggedIn)
            return new BankAccess(requests);
        else
            throw new LoginFailed("Something went wrong");
    }

}
