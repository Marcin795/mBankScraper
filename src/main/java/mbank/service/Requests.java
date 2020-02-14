package mbank.service;

import mbank.model.Credentials;
import mbank.payload.request.FinalizeAuthorizationRequest;
import mbank.payload.request.InitPrepareRequest;
import mbank.payload.request.LoginRequest;
import mbank.payload.request.StatusRequest;
import mbank.payload.response.*;
import mbank.util.Http;
import okhttp3.Headers;

import java.time.LocalDateTime;

public class Requests {

    private static final String X_REQUEST_VERIFICATION_TOKEN = "X-Request-Verification-Token";
    private final Http http;

    public Requests(Http http) {
        this.http = http;
    }

    public LoginResponse getJsonLogin(Credentials credentials) {
        var payload = new LoginRequest(credentials);
            return http.post("/pl/LoginMain/Account/JsonLogin", LoginResponse.class, payload).body;
    }

    public String fetchVerificationToken() {
        var response = http.get("/api/app/setup/data", SetupDataResponse.class);
        return response.body.antiForgeryToken;
    }

    public String fetchAuthorizationId() {
        var response = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        return response.body.scaAuthorizationId;
    }

    public String fetchTranId(String verificationToken, String authorizationId) {
        var payload = new InitPrepareRequest(authorizationId);
        var headers = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        var response = http.post("/api/auth/initprepare", InitPrepareResponse.class, payload, headers);
        return response.body.tranId;
    }

    public String getStatus(String tranId) {
        var response = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(tranId));
        return response.body.status;
    }

    public void execute(String verificationToken) {
        var header = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        http.post("/api/auth/execute", header);
    }

    public static Headers createSinleEntryHeaders(String key, String value) {
        return new Headers.Builder()
                .add(key, value)
                .build();
    }

    public void finalizeAuthorization(String authorizationId) {
        var payload = new FinalizeAuthorizationRequest(authorizationId);
        http.post("/pl/Sca/FinalizeAuthorization", payload);
    }

    public boolean isLoggedIn() {
        var response = http.get("/api/chat/init?_=" + LocalDateTime.now());
        return response.status == 200;
    }

    public AccountsListResponse getAccountList() {
        var response = http.post("/pl/Accounts/Accounts/List", AccountsListResponse.class);
        return response.body;
    }

}
