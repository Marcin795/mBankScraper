package unit.mbank.service;

import model.Credentials;
import unit.mbank.model.request.FinalizeAuthorizationRequestBody;
import unit.mbank.model.request.InitPrepareRequestBody;
import unit.mbank.model.request.LoginRequestBody;
import unit.mbank.model.request.StatusRequestBody;
import unit.mbank.model.response.*;
import util.Http;
import okhttp3.Headers;

import java.time.LocalDateTime;

public class Requests {

    private static final String X_REQUEST_VERIFICATION_TOKEN = "X-Request-Verification-Token";
    private final String BANK_ADDRESS;
    private final Http http;

    public Requests(Http http, String bankAddress) {
        this.http = http;
        BANK_ADDRESS = bankAddress;
    }

    public LoginResponseBody getJsonLogin(Credentials credentials) {
        var payload = new LoginRequestBody(credentials);
        return http.post(BANK_ADDRESS + "/pl/LoginMain/Account/JsonLogin", LoginResponseBody.class, payload);
    }

    public String fetchVerificationToken() {
        var response = http.get(BANK_ADDRESS + "/api/app/setup/data", SetupDataResponseBody.class);
        return response.antiForgeryToken;
    }

    public String fetchAuthorizationId() {
        var response = http.post(BANK_ADDRESS + "/pl/Sca/GetScaAuthorizationData", ScaDataResponseBody.class);
        return response.scaAuthorizationId;
    }

    public String fetchTranId(String verificationToken, String authorizationId) {
        var payload = new InitPrepareRequestBody(authorizationId);
        var headers = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        var response = http.post(BANK_ADDRESS + "/api/auth/initprepare", InitPrepareResponseBody.class, payload, headers);
        return response.tranId;
    }

    public String getStatus(String tranId) {
        var response = http.post(BANK_ADDRESS + "/api/auth/status", StatusResponseBody.class, new StatusRequestBody(tranId));
        return response.status;
    }

    public void execute(String verificationToken) {
        var header = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        http.post(BANK_ADDRESS + "/api/auth/execute", header);
    }

    public static Headers createSinleEntryHeaders(String key, String value) {
        return new Headers.Builder()
                .add(key, value)
                .build();
    }

    public void finalizeAuthorization(String authorizationId) {
        var payload = new FinalizeAuthorizationRequestBody(authorizationId);
        http.post(BANK_ADDRESS + "/pl/Sca/FinalizeAuthorization", payload);
    }

    public boolean isLoggedIn() {
        var status = http.get(BANK_ADDRESS + "/api/chat/init?_=" + LocalDateTime.now());
        return status == 200;
    }

    public AccountsListResponseBody getAccountList() {
        return http.post(BANK_ADDRESS + "/pl/Accounts/Accounts/List", AccountsListResponseBody.class);
    }

}
