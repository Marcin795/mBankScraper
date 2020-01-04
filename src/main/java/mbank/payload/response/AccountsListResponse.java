package mbank.payload.response;

import com.google.gson.annotations.SerializedName;
import mbank.model.AccountTypesLists;

public class AccountsListResponse {

    @SerializedName("properties")
    private final AccountTypesLists accountTypesLists;

    public AccountsListResponse(AccountTypesLists accountTypesLists) {
        this.accountTypesLists = accountTypesLists;
    }

    public AccountTypesLists getAccountTypesLists() {
        return this.accountTypesLists;
    }

}
