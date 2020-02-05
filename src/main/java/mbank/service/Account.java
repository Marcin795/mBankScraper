package mbank.service;

import mbank.payload.response.AccountsListResponse;

public class Account {

    private final Requests requests;

    public Account(Requests requests) {
        this.requests = requests;
    }

    public AccountsListResponse getAccounts() {
        return requests.getAccountList();
    }

}
