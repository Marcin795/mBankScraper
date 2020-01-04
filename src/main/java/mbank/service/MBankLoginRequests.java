package mbank.service;

import mbank.payload.request.FinalizeAuthorizationRequest;
import mbank.payload.request.InitPrepareRequest;
import mbank.payload.request.LoginRequest;
import mbank.payload.request.StatusRequest;
import mbank.payload.response.*;
import mbank.util.Http;
import okhttp3.Headers;

import java.time.LocalDateTime;

class MBankLoginRequests {

    private final Http http;
    private final SessionParams sessionParams;

    public MBankLoginRequests(Http http, SessionParams sessionParams) {
        this.http = http;
        this.sessionParams = sessionParams;
    }

    Response<LoginResponse> getJsonLogin(String username, String password) {
        var r = http.post("/pl/LoginMain/Account/JsonLogin",
                LoginResponse.class, new LoginRequest(username, password));
        sessionParams.setXTabId(r.getBody().getTabId());
        return r;
    }

    void queryForSetupData() {
        var r = http.get("/api/app/setup/data", SetupDataResponse.class);
        sessionParams.setXRequestVerificationToken(r.getBody().getAntiForgeryToken());
    }

    void queryForScaAuthorizationData() {
        var r = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        sessionParams.setScaAuthorizationId(r.getBody().getScaAuthorizationId());
    }

    void queryForInitPrepare() {
        var r = http.post("/api/auth/initprepare", InitPrepareResponse.class,
                new InitPrepareRequest(sessionParams.getScaAuthorizationId()),
                new Headers.Builder().add("X-Request-Verification-Token", sessionParams.getXRequestVerificationToken()).build());
        sessionParams.setTranId(r.getBody().getTranId());
    }

    String getStatus() {
        var r = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(sessionParams.getTranId()));
        return r.getBody().getStatus();
    }

    void execute() {
        http.post("/api/auth/execute", null,
                new Headers.Builder().add("X-Request-Verification-Token", sessionParams.getXRequestVerificationToken()).build());
    }

    void finalizeAuthorization() {
        http.post("/pl/Sca/FinalizeAuthorization", null, new FinalizeAuthorizationRequest(sessionParams.getScaAuthorizationId()),
                new Headers.Builder().add("X-Tab-Id", sessionParams.getXTabId()).build());
    }

    boolean isLoggedIn() {
        var r = http.get("/api/chat/init?_=" + LocalDateTime.now(), null);
        return r.getStatus() == 200;
    }

}
