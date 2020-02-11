package mbank;

import mbank.exceptions.InvalidCredentials;
import mbank.model.Credentials;
import mbank.service.ApiProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProviderTests {

    @Test
    void checkLoginInvalidCredentials() {
        var provider = new ApiProvider();
        var credentials = new Credentials("AdamMałysz", "JestemR4jdowcem");
        Assertions.assertThrows(InvalidCredentials.class, () -> provider.logIn(credentials));
    }

}
