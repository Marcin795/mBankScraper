package mbank;

import mbank.exceptions.InvalidCredentials;
import mbank.service.ApiProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProviderTests {

    @Test
    void checkLoginInvalidCredentials() {
        var provider = new ApiProvider();
        Assertions.assertThrows(InvalidCredentials.class, () -> provider.logIn("AdamMałysz", "JestemR4jdowcem"));
    }

}
