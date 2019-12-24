package mbank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mbank.payload.response.AccountsListResponse;
import mbank.util.Http;
import okhttp3.Headers;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class MBankAccountDataRequests {

    Http http;
    SessionParams sessionParams;

    AccountsListResponse getAccountList() throws IOException {
        var r = http.post("/pl/Accounts/Accounts/List", AccountsListResponse.class,
                new Headers.Builder().add("X-Tab-Id", sessionParams.getXTabId()));
        return r.getBody();
    }

}
