package mbank.model.payload.response;

import com.google.gson.annotations.SerializedName;
import mbank.model.AccountTypesLists;

public class AccountsListResponseBody {

    @SerializedName("properties")
    public final AccountTypesLists accountTypesLists;

    public AccountsListResponseBody(AccountTypesLists accountTypesLists) {
        this.accountTypesLists = accountTypesLists;
    }

}
