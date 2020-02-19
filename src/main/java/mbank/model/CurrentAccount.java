package mbank.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CurrentAccount {

    @SerializedName("cProductName")
    public final String accountName;

    @SerializedName("cAccountNumberForDisp")
    public final String accountNumber;

    @SerializedName("mBalance")
    public final BigDecimal balance;

    @SerializedName("cCurrency")
    public final String currency;

    public CurrentAccount(String accountName, String accountNumber, BigDecimal balance, String currency) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }
}
