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

}
