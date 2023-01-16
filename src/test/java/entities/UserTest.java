package entities;

import TestEnvironment.TestEnvironment;
import errorhandling.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest extends TestEnvironment {

    @Test
    public void createUserWithPasswordBelowMinimumLengthTest() {
        assertThrows(InvalidPasswordException.class, () -> {
            new User(
                    faker.name().username(),
                    faker.letterify("???"),
                    faker.number().numberBetween(13, 120)
            );
        });
    }

    @Test
    public void createUserPasswordWithExactlyMinimumLengthTest() {
        assertDoesNotThrow(() -> {
            new User(
                    faker.name().username(),
                    faker.letterify("????"),
                    faker.number().numberBetween(13, 120)
            );
        });
    }
}
