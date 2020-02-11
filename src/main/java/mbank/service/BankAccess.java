package mbank.service;

import mbank.model.CurrentAccount;

import java.util.List;

public class BankAccess {

    private final Requests requests;

    public BankAccess(Requests requests) {
        this.requests = requests;
    }

    public List<CurrentAccount> getAccounts() {
        var response = requests.getAccountList();
        return response.accountTypesLists.currentAccounts;
    }

}
