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
    @DisplayName("Should accept valid email addresses")
    void testValidMail() {
        assertTrue(userService.isMailValid("simon@thws.de"));
        assertTrue(userService.isMailValid("test.user@sub.domain.com"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "simonthws", "@thws.de", "simon@.de"})
    @DisplayName("Should reject invalid emails")
    void testInvalidMail(String email) {
        assertFalse(userService.isMailValid(email));
        assertFalse(userService.isMailValid(null));
    }

    @Test
    @DisplayName("Password should match pattern and not equal email")
    void testValidPassword() {
        String mail = "simon@thws.de";
        String validPw = "Secure1234567!";
        assertTrue(userService.isPasswordValid(validPw, mail));
    }

    @Test
    @DisplayName("Should fail if password is identical to email")
    void testPasswordEqualsMail() {
        String mail = "simon@thws.de";
        assertFalse(userService.isPasswordValid(mail, mail));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "password", "ONLYUPPER1!", "onlyspecial@@@"})
    @DisplayName("Should reject weak passwords")
    void testWeakPasswords(String weakPw) {
        assertFalse(userService.isPasswordValid(weakPw, "user@test.de"));
    }

    @Test
    @DisplayName("Should hash and verify password correctly")
    void testBcryptHashing() {
        String rawPassword = "MySecurePassword123!";
        String passwordHash = BcryptUtil.bcryptHash(rawPassword);

        assertNotEquals(rawPassword, passwordHash);
        assertTrue(passwordHash.startsWith("$2"));
        assertTrue(BcryptUtil.matches(rawPassword, passwordHash));
        assertFalse(BcryptUtil.matches("wrongPassword", passwordHash));
    }

    @Test
    @DisplayName("Two hashes of the same password should be different (Salt check)")
    void testSaltUniqueness() {
        String password = "SamePassword";
        String hash1 = BcryptUtil.bcryptHash(password);
        String hash2 = BcryptUtil.bcryptHash(password);

        assertNotEquals(hash1, hash2);
        assertTrue(BcryptUtil.matches(password, hash1));
        assertTrue(BcryptUtil.matches(password, hash2));
    }

    @Test
    @DisplayName("Bcrypt hash should always be exactly 60 characters long")
    void testHashLength() {
        String hash1 = BcryptUtil.bcryptHash("abc");
        String hash2 = BcryptUtil.bcryptHash("AVeryLongPasswordWithManyCharacters123456789!");

        assertEquals(60, hash1.length());
        assertEquals(60, hash2.length());
    }
}