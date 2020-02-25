package mbank.service;

import model.Credentials;
import mbank.model.request.FinalizeAuthorizationRequestBody;
import mbank.model.request.InitPrepareRequestBody;
import mbank.model.request.LoginRequestBody;
import mbank.model.request.StatusRequestBody;
import mbank.model.response.*;
import util.Http;
import okhttp3.Headers;

import java.time.LocalDateTime;

public class Requests {

    private static final String X_REQUEST_VERIFICATION_TOKEN = "X-Request-Verification-Token";
    private static final String bankAddress = "https://online.mbank.pl";
    private final Http http;

    public Requests(Http http) {
        this.http = http;
    }

    public LoginResponseBody getJsonLogin(Credentials credentials) {
        var payload = new LoginRequestBody(credentials);
        return http.post(bankAddress + "/pl/LoginMain/Account/JsonLogin", LoginResponseBody.class, payload);
    }

    public String fetchVerificationToken() {
        var response = http.get(bankAddress + "/api/app/setup/data", SetupDataResponseBody.class);
        return response.antiForgeryToken;
    }

    public String fetchAuthorizationId() {
        var response = http.post(bankAddress + "/pl/Sca/GetScaAuthorizationData", ScaDataResponseBody.class);
        return response.scaAuthorizationId;
    }

    public String fetchTranId(String verificationToken, String authorizationId) {
        var payload = new InitPrepareRequestBody(authorizationId);
        var headers = createSingleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        var response = http.post(bankAddress + "/api/auth/initprepare", InitPrepareResponseBody.class, payload, headers);
        return response.tranId;
    }

    public String getStatus(String tranId) {
        var response = http.post(bankAddress + "/api/auth/status", StatusResponseBody.class, new StatusRequestBody(tranId));
        return response.status;
    }

    public void execute(String verificationToken) {
        var header = createSingleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        http.post(bankAddress + "/api/auth/execute", header);
    }

    private static Headers createSingleEntryHeaders(String key, String value) {
        return new Headers.Builder()
                .add(key, value)
                .build();
    }

    public void finalizeAuthorization(String authorizationId) {
        var payload = new FinalizeAuthorizationRequestBody(authorizationId);
        http.post(bankAddress + "/pl/Sca/FinalizeAuthorization", payload);
    }

    public boolean isLoggedIn() {
        var status = http.get(bankAddress + "/api/chat/init?_=" + LocalDateTime.now());
        return status == 200;
    }

    public AccountsListResponseBody getAccountList() {
        return http.post(bankAddress + "/pl/Accounts/Accounts/List", AccountsListResponseBody.class);
    }

}
