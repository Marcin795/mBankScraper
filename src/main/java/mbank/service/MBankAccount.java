package mbank.service;

import mbank.payload.response.AccountsListResponse;

public class MBankAccount {

    private final MBankAccountDataRequests accountDataRequests;

    public MBankAccount(MBankAccountDataRequests accountDataRequests) {
        this.accountDataRequests = accountDataRequests;
    }

    public AccountsListResponse getAccounts() {
        return accountDataRequests.getAccountList();
    }

}
