package mbank.model;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountTypesLists {

    @SerializedName("CurrentAccountsList")
    List<CurrentAccount> currentAccounts;

}
