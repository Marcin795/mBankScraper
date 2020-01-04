package mbank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountTypesLists {

    @SerializedName("CurrentAccountsList")
    private final List<CurrentAccount> currentAccounts;

    public AccountTypesLists(List<CurrentAccount> currentAccounts) {
        this.currentAccounts = currentAccounts;
    }

    public List<CurrentAccount> getCurrentAccounts() {
        return this.currentAccounts;
    }

}
