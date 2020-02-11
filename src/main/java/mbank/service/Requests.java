package mbank.service;

import mbank.model.Credentials;
import mbank.payload.executionOrder.*;
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

    Requests(Http http) {
        this.http = http;
    }

    ParsedResponse<LoginResponse> getJsonLogin(Credentials credentials) {
        var payload = new LoginRequest(credentials);
        return http.post("/pl/LoginMain/Account/JsonLogin", LoginResponse.class, payload);
    }

    StageOne queryForSetupData(LoginResponse loginResponse) {
        var response = http.get("/api/app/setup/data", SetupDataResponse.class);
        return new StageOne(response.body.antiForgeryToken);
    }

    StageTwo queryForScaAuthorizationData(StageOne stageOne) {
        var response = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        return new StageTwo(stageOne.xRequestVerificationToken, response.body.scaAuthorizationId);
    }

    Stage2fa queryForInitPrepare(StageTwo stageTwo) {
        var payload = new InitPrepareRequest(stageTwo.scaAuthorizationId);
        var headers = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, stageTwo.xRequestVerificationToken);
        var response = http.post("/api/auth/initprepare", InitPrepareResponse.class, payload, headers);
        return new Stage2fa(stageTwo, response.body.tranId);
    }

    String getStatus(String tranId) {
        var response = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(tranId));
        return response.body.status;
    }

    StageFive execute(StageFour stageFour) {
        var header = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, stageFour.xRequestVerificationToken);
        http.post("/api/auth/execute", header);
        return new StageFive(stageFour);
    }

    StageSix finalizeAuthorization(StageFive stageFive) {
        var payload = new FinalizeAuthorizationRequest(stageFive.scaAuthorizationId);
        http.post("/pl/Sca/FinalizeAuthorization", payload);
        return new StageSix();
    }

    StageSeven isLoggedIn(StageSix stageSix) {
        var response = http.get("/api/chat/init?_=" + LocalDateTime.now());
        return new StageSeven(response.status == 200);
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
