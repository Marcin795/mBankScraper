package mbank.service;

import mbank.payload.response.AccountsListResponse;
import mbank.payload.response.Response;
import mbank.util.Http;
import okhttp3.Headers;

class MBankAccountDataRequests {

    private final Http http;
    private final SessionParams sessionParams;

    public MBankAccountDataRequests(Http http, SessionParams sessionParams) {
        this.http = http;
        this.sessionParams = sessionParams;
    }

    AccountsListResponse getAccountList() {
        Response<AccountsListResponse> r = http.post("/pl/Accounts/Accounts/List", AccountsListResponse.class,
                new Headers.Builder().add("X-Tab-Id", sessionParams.getXTabId()));
        return r.getBody();
    }

}
