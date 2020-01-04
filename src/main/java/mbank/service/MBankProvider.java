package mbank.service;

import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.util.Http;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;

import java.net.CookieManager;
import java.net.CookiePolicy;

public class MBankProvider {

    private final MBankLoginRequests loginRequests;
    private final MBankAccountDataRequests accountDataRequests;

    public MBankProvider() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        OkHttpClient client = new OkHttpClient().newBuilder().cookieJar(cookieJar).build();
        SessionParams sessionParams = new SessionParams();
        Http http = new Http(client, "https://online.mbank.pl");
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
        if(StringUtils.isBlank(username))
            throw new InvalidCredentials("Username can't be blank");
        if(StringUtils.isBlank(password))
            throw new InvalidCredentials("Password can't be blank");
    }

    private void initializeLogin(String username, String password) {
        var loginResponse = loginRequests.getJsonLogin(username, password);
        if(!loginResponse.getBody().isSuccessful())
            throw new InvalidCredentials("Passed credentials are invalid.");
        loginRequests.queryForSetupData();
        loginRequests.queryForScaAuthorizationData();
        loginRequests.queryForInitPrepare();
    }

    private void awaitTwoFactorConfirmation() {
        System.out.println("Waiting for 2fa confirmation...");
        String status = loginRequests.getStatus();
        for(int i = 0; i < 60 && !status.matches("Authorized|Canceled"); i++, status = loginRequests.getStatus())
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        verifyTwoFactorStatus(status);
    }

    private void verifyTwoFactorStatus(String status) {
        switch(status) {
            case "Authorized":
                System.out.println("Login authorized.");
                break;
            case "Canceled":
                throw new LoginFailed("2fa Cancelled");
            default:
                throw new LoginFailed("2fa Timeout");
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
