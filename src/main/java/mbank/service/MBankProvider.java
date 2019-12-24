package mbank.service;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import mbank.exceptions.InvalidCredentialsException;
import mbank.exceptions.LoginFailedException;
import mbank.util.Http;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static mbank.util.MBankConstants.MBANK_BASE_URL;
import static mbank.util.MBankConstants.TWO_FACTOR_REPEAT_LIMIT;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MBankProvider {

    MBankLoginRequests loginRequests;
    MBankAccountDataRequests accountDataRequests;

    public MBankProvider() {
        Gson gson = new Gson();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        OkHttpClient client = new OkHttpClient().newBuilder().cookieJar(cookieJar).build();
        SessionParams sessionParams = new SessionParams();
        Http http = new Http(client, gson, MBANK_BASE_URL);
        loginRequests = new MBankLoginRequests(http, sessionParams);
        accountDataRequests = new MBankAccountDataRequests(http, sessionParams);
    }

    public MBankAccount login(String username, String password) {
        try {
            initializeLogin(username, password);
            awaitTwoFactorConfirmation();
            if (finalizeLogin()) {
                return new MBankAccount(accountDataRequests);
            } else {
                throw new LoginFailedException("Something went wrong");
            }
        } catch (IOException e) {
            throw new LoginFailedException("Something went wrong");
        }
    }

    private void initializeLogin(String username, String password) throws IOException {
        var loginResponse = loginRequests.loginJson(username, password);
        if(!loginResponse.getBody().isSuccessful()) {
            throw new InvalidCredentialsException();
        }
        loginRequests.setupData();
        loginRequests.scaAuthorizationData();
        loginRequests.initPrepare();
    }

    private void awaitTwoFactorConfirmation() throws IOException {
        System.out.println("Waiting for 2fa confirmation...");
        String status = loginRequests.status();
        for(int i = 0; i < TWO_FACTOR_REPEAT_LIMIT && !status.matches("Authorized|Canceled"); i++, status = loginRequests.status()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        if(status.equals("Authorized")) {
            System.out.println("Login authorized.");
            return;
        }
        if(status.equals("Canceled")) {
            throw new LoginFailedException("2fa Cancelled");
        }
        throw new LoginFailedException("2fa Timeout");
    }

    private boolean finalizeLogin() throws IOException {
        loginRequests.execute();
        loginRequests.finalizeAuthorization();
        return loginRequests.isLoggedIn();
    }

}
