package mbank.service;

import exceptions.InvalidCredentials;
import exceptions.LoginFailed;
import mbank.model.response.LoginResponseBody;
import util.CommandLine;
import util.Delays;
import model.Credentials;

import java.util.Set;

public class LoginCommand {

    private static final String CANCELED = "Canceled";
    private static final String AUTHORIZED = "Authorized";
    private static final int MAX_RETRIES = 60;
    private final Requests requests;
    private final Delays delays;
    private final CommandLine cli;

    public LoginCommand(Requests requests, Delays delays, CommandLine cli) {
        this.requests = requests;
        this.delays = delays;
        this.cli = cli;
    }

    public BankAccess logIn(Credentials credentials) {
        var loginResponse = requests.getJsonLogin(credentials);
        checkLoginSuccessful(loginResponse);
        var verificationToken = requests.fetchVerificationToken();
        var authorizationId = requests.fetchAuthorizationId();
        var tranId = requests.fetchTranId(verificationToken, authorizationId);
        awaitTwoFactorConfirmation(tranId);
        return finalizeLogin(verificationToken, authorizationId);
    }

    private static void checkLoginSuccessful(LoginResponseBody loginResponse) {
        if(!loginResponse.successful && loginResponse.errorMessageTitle.equals("Nieprawidłowy identyfikator lub hasło."))
            throw new InvalidCredentials("Passed credentials are invalid.");
    }

    private void awaitTwoFactorConfirmation(String tranId) {
        cli.twoFactorPrompt();
        var status = checkStatus(tranId);
        verifyTwoFactorStatus(status);
    }

    private String checkStatus(String tranId) {
        String status = requests.getStatus(tranId);
        for(int tries = 0; tries < MAX_RETRIES; tries++) {
            if(isStatusSet(status))
                break;
            delays.waitOneSecond();
            status = requests.getStatus(tranId);
        }
        return status;
    }

    private static boolean isStatusSet(String status) {
        return Set.of(AUTHORIZED, CANCELED).contains(status);
    }

    private static void verifyTwoFactorStatus(String status) {
        switch(status) {
            case AUTHORIZED:
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

}
