package mbank.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountTypesLists {

    @SerializedName("CurrentAccountsList")
    public final List<Account> accounts;

    public AccountTypesLists(List<Account> accounts) {
        this.accounts = accounts;
    }

}
