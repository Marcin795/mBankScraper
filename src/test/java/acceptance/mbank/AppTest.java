package acceptance.mbank;

import app.App;
import exceptions.InvalidCredentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AppTest {

    @Test
    void invalidCredentialsThrowException() {
        assertThrows(InvalidCredentials.class, () -> App.main("AdamMałysz", "JestemR4jdowcem"));
    }

}
