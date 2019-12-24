package mbank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mbank.exceptions.BankAccountException;
import mbank.payload.response.AccountsListResponse;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MBankAccount {

    MBankAccountDataRequests accountDataRequests;

    public AccountsListResponse getAccounts() {
        try {
            return accountDataRequests.getAccountList();
        } catch (IOException e) {
            throw new BankAccountException("Couldn't get accounts list");
        }
    }

}
