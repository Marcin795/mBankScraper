package mbank.service;

import mbank.exceptions.InvalidCredentials;
import mbank.exceptions.LoginFailed;
import mbank.model.Credentials;
import mbank.payload.executionOrder.Stage2fa;
import mbank.payload.executionOrder.StageFour;
import mbank.payload.executionOrder.StageSix;
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
        var stage2fa = initializeLogin(credentials);
        var stageFour = awaitTwoFactorConfirmation(stage2fa);
        return finalizeLogin(stageFour);
    }

    private Stage2fa initializeLogin(Credentials credentials) {
        var loginResponse = requests.getJsonLogin(credentials);
        checkLoginSuccessful(loginResponse.body.successful);
        var stageOne = requests.queryForSetupData(loginResponse.body);
        var stageTwo = requests.queryForScaAuthorizationData(stageOne);
        return requests.queryForInitPrepare(stageTwo);
    }

    private static void checkLoginSuccessful(boolean successful) {
        if(!successful)
            throw new InvalidCredentials("Passed credentials are invalid.");
    }

    private StageFour awaitTwoFactorConfirmation(Stage2fa stage2fa) {
        System.out.println("Waiting for 2FA confirmation...");
        String status = requests.getStatus(stage2fa.tranId);
        for(int tries = 0; tries++ < 60 && statusNotSet(status); status = requests.getStatus(stage2fa.tranId))
            waitOneSecond();
        verifyTwoFactorStatus(status);
        return new StageFour(stage2fa);
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
                System.out.println("Login authorized.");
                break;
            case CANCELED:
                throw new LoginFailed("2FA Canceled");
            default:
                throw new LoginFailed("2FA Timeout");
        }
    }

    private BankAccess finalizeLogin(StageFour stageFour) {
        var stageFive = requests.execute(stageFour);
        StageSix stageSix = requests.finalizeAuthorization(stageFive);
        if(requests.isLoggedIn(stageSix).loggedIn)
            return new BankAccess(requests);
        else
            throw new LoginFailed("Something went wrong");
    }

}
