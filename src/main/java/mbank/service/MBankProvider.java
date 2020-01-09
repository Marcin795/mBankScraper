package mbank.service;

import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.util.Http;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class MBankProvider {

    private final MBankLoginRequests loginRequests;
    private final MBankAccountDataRequests accountDataRequests;

    public MBankProvider() {
        SessionParams sessionParams = new SessionParams();
        Http http = new Http();
        loginRequests = new MBankLoginRequests(http, sessionParams);
        accountDataRequests = new MBankAccountDataRequests(http, sessionParams);
    }

    public MBankAccount logIn(String username, String password) {
        checkCredentials(username, password);
        initializeLogin(username, password);
        awaitTwoFactorConfirmation();
        return finalizeLogin();
    }

    private void checkCredentials(String username, String password) {
        if(isBlank(username))
            throw new InvalidCredentials("Username can't be blank");
        if(isBlank(password))
            throw new InvalidCredentials("Password can't be blank");
    }

    private void initializeLogin(String username, String password) {
        var loginResponse = loginRequests.getJsonLogin(username, password);
        checkLoginSuccessful(loginResponse.body.successful);
        loginRequests.queryForSetupData();
        loginRequests.queryForScaAuthorizationData();
        loginRequests.queryForInitPrepare();
    }

    private void checkLoginSuccessful(boolean successful) {
        if(!successful)
            throw new InvalidCredentials("Passed credentials are invalid.");
    }

    private void awaitTwoFactorConfirmation() {
        System.out.println("Waiting for 2FA confirmation...");
        String status = loginRequests.getStatus();
        for(int i = 0; i < 60 && !checkStatus(status); i++, status = loginRequests.getStatus())
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new RuntimeException(e);
            }
        verifyTwoFactorStatus(status);
    }

    private boolean checkStatus(String status) {
        return status.matches("Authorized|Canceled");
    }

    private void verifyTwoFactorStatus(String status) {
        switch(status) {
            case "Authorized":
                System.out.println("Login authorized.");
                break;
            case "Canceled":
                throw new LoginFailed("2FA Canceled");
            default:
                throw new LoginFailed("2FA Timeout");
        }
    }

    private MBankAccount finalizeLogin() {
        loginRequests.execute();
        loginRequests.finalizeAuthorization();
        if(loginRequests.isLoggedIn())
            return new MBankAccount(accountDataRequests);
        else
            throw new LoginFailed("Something went wrong");
    }

}
