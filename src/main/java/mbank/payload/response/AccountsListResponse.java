package mbank.payload.response;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import mbank.model.AccountTypesLists;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountsListResponse {

    String name;

    @SerializedName("properties")
    AccountTypesLists accountTypesLists;

}
