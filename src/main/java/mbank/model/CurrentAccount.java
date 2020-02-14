package mbank.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CurrentAccount {

    @SerializedName("cProductName")
    public String accountName;

    @SerializedName("cAccountNumberForDisp")
    public String accountNumber;

    @SerializedName("mBalance")
    public BigDecimal balance;

    @SerializedName("cCurrency")
    public String currency;

    public CurrentAccount(String accountName, String accountNumber, BigDecimal balance, String currency) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }
}
