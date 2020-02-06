package mbank.model;

import com.google.gson.annotations.SerializedName;

public class CurrentAccount {

    @SerializedName("cProductName")
    public String accountName;

    @SerializedName("cAccountNumberForDisp")
    public String accountNumber;

    @SerializedName("mBalance")
    public double balance;

    @SerializedName("cCurrency")
    public String currency;

}
