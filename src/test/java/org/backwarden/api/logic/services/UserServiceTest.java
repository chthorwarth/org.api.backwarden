package org.backwarden.api.logic.services;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private final UserService userService = new UserService();


    @Test
    @DisplayName("Should hash and verify password correctly")
    void testBcryptHashing() {
        String rawPassword = "MySecurePassword123!";
        String passwordHash = userService.getPasswordHash(rawPassword);

        assertNotEquals(rawPassword, passwordHash);
        assertTrue(passwordHash.startsWith("$2"));
        assertTrue(BcryptUtil.matches(rawPassword, passwordHash));
        assertFalse(BcryptUtil.matches("wrongPassword", passwordHash));
    }

    @Test
    @DisplayName("Two hashes of the same password should be different (Salt check)")
    void testSaltUniqueness() {
        String password = "SamePassword";
        String hash1 = userService.getPasswordHash(password);
        String hash2 = userService.getPasswordHash(password);

        assertNotEquals(hash1, hash2);
        assertTrue(BcryptUtil.matches(password, hash1));
        assertTrue(BcryptUtil.matches(password, hash2));
    }

    @Test
    @DisplayName("Bcrypt hash should always be exactly 60 characters long")
    void testHashLength() {
        String hash1 = userService.getPasswordHash("abc");
        String hash2 = userService.getPasswordHash("AVeryLongPasswordWithManyCharacters123456789!");

        assertEquals(60, hash1.length());
        assertEquals(60, hash2.length());
    }
}