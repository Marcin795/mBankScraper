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

    VerificationToken queryForSetupData(LoginResponse loginResponse) {
        var response = http.get("/api/app/setup/data", SetupDataResponse.class);
        return new VerificationToken(response.body.antiForgeryToken);
    }

    VerificationTokenAndAuthorizationId queryForScaAuthorizationData(VerificationToken verificationToken) {
        var response = http.post("/pl/Sca/GetScaAuthorizationData", ScaDataResponse.class);
        return new VerificationTokenAndAuthorizationId(verificationToken.xRequestVerificationToken, response.body.scaAuthorizationId);
    }

    VerificationTokenAndAuthorizationTokenAndTranId queryForInitPrepare(VerificationTokenAndAuthorizationId verificationTokenAndAuthorizationId) {
        var payload = new InitPrepareRequest(verificationTokenAndAuthorizationId.scaAuthorizationId);
        var headers = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationTokenAndAuthorizationId.xRequestVerificationToken);
        var response = http.post("/api/auth/initprepare", InitPrepareResponse.class, payload, headers);
        return new VerificationTokenAndAuthorizationTokenAndTranId(verificationTokenAndAuthorizationId, response.body.tranId);
    }

    String getStatus(String tranId) {
        var response = http.post("/api/auth/status", StatusResponse.class, new StatusRequest(tranId));
        return response.body.status;
    }

    AuthorizationId execute(VerificationTokenAndAuthorizationTokenWithoutTranId verificationTokenAndAuthorizationTokenWithoutTranId) {
        var header = createSinleEntryHeaders(X_REQUEST_VERIFICATION_TOKEN, verificationTokenAndAuthorizationTokenWithoutTranId.xRequestVerificationToken);
        http.post("/api/auth/execute", header);
        return new AuthorizationId(verificationTokenAndAuthorizationTokenWithoutTranId);
    }

    JustEmptyClassToForceProperUsage finalizeAuthorization(AuthorizationId authorizationId) {
        var payload = new FinalizeAuthorizationRequest(authorizationId.scaAuthorizationId);
        http.post("/pl/Sca/FinalizeAuthorization", payload);
        return new JustEmptyClassToForceProperUsage();
    }

    StageSeven isLoggedIn(JustEmptyClassToForceProperUsage stageSix) {
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
