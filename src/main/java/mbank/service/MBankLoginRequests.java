package mbank.service;

import mbank.payload.request.FinalizeAuthorizationRequest;
import mbank.payload.request.InitPrepareRequest;
import mbank.payload.request.LoginRequest;
import mbank.payload.request.StatusRequest;
import mbank.payload.response.*;
import mbank.util.Http;
import okhttp3.Headers;

import static java.time.LocalDateTime.now;

class MBankLoginRequests {

    private static final String X_REQUEST_VERIFICATION_TOKEN = "X-Request-Verification-Token";
    private static final String X_TAB_ID = "X-Tab-Id";
    private final Http http;
    private final SessionParams sessionParams;

    MBankLoginRequests(Http http, SessionParams sessionParams) {
        this.http = http;
        this.sessionParams = sessionParams;
    }

    Response<LoginResponse> getJsonLogin(String username, String password) {
        var r = http.post("/pl/LoginMain/Account/JsonLogin",
                LoginResponse.class, new LoginRequest(username, password));
        sessionParams.xTabId = r.body.tabId;
        return r;
    }

    void queryForSetupData() {
        var r = http.get("/api/app/setup/data", SetupDataResponse.class);
        sessionParams.xRequestVerificationToken = r.body.antiForgeryToken;
    }

    void queryForScaAuthorizationData() {
        var r = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        sessionParams.scaAuthorizationId = r.body.scaAuthorizationId;
    }

    void queryForInitPrepare() {
        var r = http.post("/api/auth/initprepare", InitPrepareResponse.class,
                new InitPrepareRequest(sessionParams.scaAuthorizationId), getXRequestVerificationTokenHeader());
        sessionParams.tranId = r.body.tranId;
    }

    String getStatus() {
        var r = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(sessionParams.tranId));
        return r.body.status;
    }

    void execute() {
        http.post("/api/auth/execute", null, getXRequestVerificationTokenHeader());
    }

    void finalizeAuthorization() {
        http.post("/pl/Sca/FinalizeAuthorization", null, new FinalizeAuthorizationRequest(sessionParams.scaAuthorizationId),
                getXTabIdHeader());
    }

    boolean isLoggedIn() {
        var r = http.get("/api/chat/init?_=" + now(), null);
        return r.status == 200;
    }

    private Headers getXRequestVerificationTokenHeader() {
        return new Headers.Builder().add(X_REQUEST_VERIFICATION_TOKEN, sessionParams.xRequestVerificationToken).build();
    }

    private Headers getXTabIdHeader() {
        return new Headers.Builder().add(X_TAB_ID, sessionParams.xTabId).build();
    }

}
