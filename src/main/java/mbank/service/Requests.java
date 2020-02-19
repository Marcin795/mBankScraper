package mbank.service;

import model.Credentials;
import mbank.model.payload.request.FinalizeAuthorizationRequestBody;
import mbank.model.payload.request.InitPrepareRequestBody;
import mbank.model.payload.request.LoginRequestBody;
import mbank.model.payload.request.StatusRequestBody;
import mbank.model.payload.response.*;
import util.Http;
import okhttp3.Headers;

import java.time.LocalDateTime;

public class Requests {

    private static final String X_REQUEST_VERIFICATION_TOKEN = "X-Request-Verification-Token";
    private final Http http;

    public Requests(Http http) {
        this.http = http;
    }

    public LoginResponseBody getJsonLogin(Credentials credentials) {
        var payload = new LoginRequestBody(credentials);
            return http.post("/pl/LoginMain/Account/JsonLogin", LoginResponseBody.class, payload).body;
    }

    public String fetchVerificationToken() {
        var response = http.get("/api/app/setup/data", SetupDataResponseBody.class);
        return response.body.antiForgeryToken;
    }

    public String fetchAuthorizationId() {
        var response = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponseBody.class);
        return response.body.scaAuthorizationId;
    }

    public String fetchTranId(String verificationToken, String authorizationId) {
        var payload = new InitPrepareRequestBody(authorizationId);
        var headers = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationToken);
        var response = http.post("/api/auth/initprepare", InitPrepareResponseBody.class, payload, headers);
        return response.body.tranId;
    }

    public String getStatus(String tranId) {
        var response = http.post("/api/auth/status", StatusResponseBody.class, new StatusRequestBody(tranId));
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
        var payload = new FinalizeAuthorizationRequestBody(authorizationId);
        http.post("/pl/Sca/FinalizeAuthorization", payload);
    }

    public boolean isLoggedIn() {
        var response = http.get("/api/chat/init?_=" + LocalDateTime.now());
        return response.status == 200;
    }

    public AccountsListResponseBody getAccountList() {
        var response = http.post("/pl/Accounts/Accounts/List", AccountsListResponseBody.class);
        return response.body;
    }

}
