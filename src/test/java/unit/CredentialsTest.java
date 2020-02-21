package unit;

import exceptions.InvalidInput;
import model.Credentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsTest {

    @Test
    void newCredentialsThrowsInvalidInputWithMoreOrLessParametersThanTwo() {
        assertThrows(InvalidInput.class, () -> new Credentials("user"));
        assertThrows(InvalidInput.class, () -> new Credentials("user", "pass", "someRandomArgument"));
    }

    @Test
    void newCredentialsDoesntThrowInvalidInputWithExactlyTwoParameters() {
        assertDoesNotThrow(() -> new Credentials("user", "pass"));
    }

    @Test
    void newCredentialsMapsProperly() {
        var expectedUsername = "user";
        var expectedPassword = "pass";
        var credentials = new Credentials(expectedUsername, expectedPassword);
        var actualUsername = credentials.username;
        var actualPassword = credentials.password;
        assertEquals(actualUsername, actualUsername);
        assertEquals(expectedPassword, actualPassword);
    }

}