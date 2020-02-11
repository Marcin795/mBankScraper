package mbank.service;

import mbank.payload.request.FinalizeAuthorizationRequest;
import mbank.payload.request.InitPrepareRequest;
import mbank.payload.request.LoginRequest;
import mbank.payload.request.StatusRequest;
import mbank.payload.response.*;
import mbank.util.Http;
import okhttp3.Headers;

import java.time.LocalDateTime;

class Requests {

    private static final String X_REQUEST_VERIFICATION_TOKEN = "X-Request-Verification-Token";
    private final Http http;
    private final SessionParams sessionParams;

    Requests(Http http, SessionParams sessionParams) {
        this.http = http;
        this.sessionParams = sessionParams;
    }

    ParsedResponse<LoginResponse> getJsonLogin(String username, String password) {
        var response = http.post("/pl/LoginMain/Account/JsonLogin",
                LoginResponse.class, new LoginRequest(username, password));
        return response;
    }

    void queryForSetupData() {
        var response = http.get("/api/app/setup/data", SetupDataResponse.class);
        sessionParams.xRequestVerificationToken = response.body.antiForgeryToken;
    }

    void queryForScaAuthorizationData() {
        var response = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        sessionParams.scaAuthorizationId = response.body.scaAuthorizationId;
    }

    void queryForInitPrepare() {
        var response = http.post("/api/auth/initprepare", InitPrepareResponse.class,
                new InitPrepareRequest(sessionParams.scaAuthorizationId), getXRequestVerificationTokenHeader());
        sessionParams.tranId = response.body.tranId;
    }

    String getStatus() {
        var response = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(sessionParams.tranId));
        return response.body.status;
    }

    void execute() {
        http.post("/api/auth/execute", getXRequestVerificationTokenHeader());
    }

    void finalizeAuthorization() {
        var payload = new FinalizeAuthorizationRequest(sessionParams.scaAuthorizationId);
        http.post("/pl/Sca/FinalizeAuthorization", payload);
    }

    boolean isLoggedIn() {
        var response = http.get("/api/chat/init?_=" + LocalDateTime.now());
        return response.status == 200;
    }

    private Headers getXRequestVerificationTokenHeader() {
        return createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, sessionParams.xRequestVerificationToken);
    }

    private static Headers createSinleEntryHeaders(String key, String value) {
        return new Headers.Builder()
                .add(key, value)
                .build();
    }

    AccountsListResponse getAccountList() {
        var response = http.post("/pl/Accounts/Accounts/List", AccountsListResponse.class);
        return response.body;
    }

}
