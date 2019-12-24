package mbank.model;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrentAccount {

    @SerializedName("cProductName")
    String accountName;

    @SerializedName("cAccountNumberForDisp")
    String accountNumber;

    @SerializedName("mBalance")
    double balance;

    @SerializedName("cCurrency")
    String currency;

}
