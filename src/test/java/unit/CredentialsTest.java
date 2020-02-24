package unit;

import exceptions.InvalidInput;
import model.Credentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsTest {

    @Test
    void throwsInvalidInputIfArgumentCountIsDifferentThanTwo() {
        assertThrows(InvalidInput.class, () -> new Credentials("user"));
        assertThrows(InvalidInput.class, () -> new Credentials("user", "pass", "someRandomArgument"));
    }

    @Test
    void doesntThrowInvalidInputWithExactlyTwoParameters() {
        assertDoesNotThrow(() -> new Credentials("user", "pass"));
    }

    @Test
    void newCredentialsMapsProperly() {
        var actual = new Credentials("user", "pass");
        assertEquals("user", actual.username);
        assertEquals("pass", actual.password);
    }

}