package mbank.service;

import mbank.model.response.Account;

import java.util.List;

public class BankAccess {

    public final Requests requests;

    BankAccess(Requests requests) {
        this.requests = requests;
    }

    public List<Account> getAccounts() {
        var response = requests.getAccountList();
        return response.accountTypesLists.accounts;
    }

}
