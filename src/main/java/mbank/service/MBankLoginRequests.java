package mbank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mbank.payload.request.FinalizeAuthorizationRequest;
import mbank.payload.request.InitPrepareRequest;
import mbank.payload.request.LoginRequest;
import mbank.payload.request.StatusRequest;
import mbank.payload.response.InitPrepareResponse;
import mbank.payload.response.LoginResponse;
import mbank.payload.response.ScaDataResponse;
import mbank.payload.response.SetupDataResponse;
import mbank.payload.response.StatusResponse;
import mbank.payload.response.Response;
import mbank.util.Http;
import okhttp3.Headers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class MBankLoginRequests {

    Http http;
    SessionParams sessionParams;

    Response<LoginResponse> loginJson(String username, String password) throws IOException {
        var r = http.post("/pl/LoginMain/Account/JsonLogin",
                LoginResponse.class, new LoginRequest(username, password));
        sessionParams.setXTabId(r.getBody().getTabId());
        return r;
    }

    void setupData() throws IOException {
        var r = http.get("/api/app/setup/data", SetupDataResponse.class);
        sessionParams.setXRequestVerificationToken(r.getBody().getAntiForgeryToken());
    }

    void scaAuthorizationData() throws IOException {
        var r = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        sessionParams.setScaAuthorizationId(r.getBody().getScaAuthorizationId());
    }

    void initPrepare() throws IOException {
        var r = http.post("/api/auth/initprepare", InitPrepareResponse.class,
                new InitPrepareRequest(sessionParams.getScaAuthorizationId()),
                new Headers.Builder().add("X-Request-Verification-Token", sessionParams.getXRequestVerificationToken()).build());
        sessionParams.setTranId(r.getBody().getTranId());
    }

    String status() throws IOException {
        var r = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(sessionParams.getTranId()));
        return r.getBody().getStatus();
    }

    void execute() throws IOException {
        http.post("/api/auth/execute", null,
                new Headers.Builder().add("X-Request-Verification-Token", sessionParams.getXRequestVerificationToken()).build());
    }

    void finalizeAuthorization() throws IOException {
        http.post("/pl/Sca/FinalizeAuthorization", null, new FinalizeAuthorizationRequest(sessionParams.getScaAuthorizationId()),
                new Headers.Builder().add("X-Tab-Id", sessionParams.getXTabId()).build());
    }

    boolean isLoggedIn() throws IOException {
        var r = http.get("/api/chat/init?_=" + LocalDateTime.now(), null);
        return r.getStatus() == 200;
    }

}
