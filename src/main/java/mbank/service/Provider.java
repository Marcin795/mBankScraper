package mbank.service;

import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.util.Http;

import java.util.Set;

public class Provider {

    private static final String CANCELED = "Canceled";
    private static final String AUTHORIZED = "Authorized";
    private final Requests requests;

    public Provider() {
        SessionParams sessionParams = new SessionParams();
        Http http = new Http();
        requests = new Requests(http, sessionParams);
    }

    public Account logIn(String username, String password) {
        checkCredentials(username, password);
        initializeLogin(username, password);
        awaitTwoFactorConfirmation();
        return finalizeLogin();
    }

    private void checkCredentials(String username, String password) {
        if(username.isBlank())
            throw new InvalidCredentials("Username can't be blank");
        if(password.isBlank())
            throw new InvalidCredentials("Password can't be blank");
    }

    private void initializeLogin(String username, String password) {
        var loginResponse = requests.getJsonLogin(username, password);
        checkLoginSuccessful(loginResponse.body.successful);
        requests.queryForSetupData();
        requests.queryForScaAuthorizationData();
        requests.queryForInitPrepare();
    }

    private void checkLoginSuccessful(boolean successful) {
        if(!successful)
            throw new InvalidCredentials("Passed credentials are invalid.");
    }

    private void awaitTwoFactorConfirmation() {
        System.out.println("Waiting for 2FA confirmation...");
        String status = requests.getStatus();
        for(int tries = 0; tries++ < 60 && statusNotSet(status); status = requests.getStatus())
            waitOneSecond();
        verifyTwoFactorStatus(status);
    }

    private void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private boolean statusNotSet(String status) {
        return !Set.of(AUTHORIZED, CANCELED).contains(status);
    }

    private void verifyTwoFactorStatus(String status) {
        switch(status) {
            case AUTHORIZED:
                System.out.println("Login authorized.");
                break;
            case CANCELED:
                throw new LoginFailed("2FA Canceled");
            default:
                throw new LoginFailed("2FA Timeout");
        }
    }

    private Account finalizeLogin() {
        requests.execute();
        requests.finalizeAuthorization();
        if(requests.isLoggedIn())
            return new Account(requests);
        else
            throw new LoginFailed("Something went wrong");
    }

}
