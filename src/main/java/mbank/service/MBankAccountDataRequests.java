package mbank.service;

import mbank.payload.response.AccountsListResponse;
import mbank.util.Http;
import okhttp3.Headers;

class MBankAccountDataRequests {

    private static final String X_TAB_ID = "X-Tab-Id";
    private final Http http;
    private final SessionParams sessionParams;

    MBankAccountDataRequests(Http http, SessionParams sessionParams) {
        this.http = http;
        this.sessionParams = sessionParams;
    }

    AccountsListResponse getAccountList() {
        var r = http.post("/pl/Accounts/Accounts/List", AccountsListResponse.class,
                new Headers.Builder().add(X_TAB_ID, sessionParams.xTabId));
        return r.body;
    }

}
