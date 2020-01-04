package mbank.model;

import com.google.gson.annotations.SerializedName;

public class CurrentAccount {

    @SerializedName("cProductName")
    private final String accountName;

    @SerializedName("cAccountNumberForDisp")
    private final String accountNumber;

    @SerializedName("mBalance")
    private final double balance;

    @SerializedName("cCurrency")
    private final String currency;

    public CurrentAccount(String accountName, String accountNumber, double balance, String currency) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getCurrency() {
        return this.currency;
    }

}
