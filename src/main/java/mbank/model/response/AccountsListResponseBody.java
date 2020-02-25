package mbank.model.response;

import com.google.gson.annotations.SerializedName;

public class AccountsListResponseBody {

    @SerializedName("properties")
    public final AccountTypesLists accountTypesLists;

    public AccountsListResponseBody(AccountTypesLists accountTypesLists) {
        this.accountTypesLists = accountTypesLists;
    }

}
