package org.backwarden.api.logic.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidationHelperTest {
    @Test
    @DisplayName("Should accept valid email addresses")
    void testValidMail() {
        Assertions.assertTrue(ValidationHelper.isMailValid("simon@thws.de"));
        Assertions.assertTrue(ValidationHelper.isMailValid("test.user@sub.domain.com"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "simonthws", "@thws.de", "simon@.de"})
    @DisplayName("Should reject invalid emails")
    void testInvalidMail(String email) {
        Assertions.assertFalse(ValidationHelper.isMailValid(email));
        Assertions.assertFalse(ValidationHelper.isMailValid(null));
    }

    @Test
    @DisplayName("Password should match pattern and not equal email")
    void testValidPassword() {
        String mail = "simon@thws.de";
        String validPw = "Secure1234567!";
        Assertions.assertTrue(ValidationHelper.isPasswordValid(validPw, mail));
    }

    @Test
    @DisplayName("Should fail if password is identical to email")
    void testPasswordEqualsMail() {
        String mail = "simon@thws.de";
        Assertions.assertFalse(ValidationHelper.isPasswordValid(mail, mail));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "password", "ONLYUPPER1!", "onlyspecial@@@"})
    @DisplayName("Should reject weak passwords")
    void testWeakPasswords(String weakPw) {
        Assertions.assertFalse(ValidationHelper.isPasswordValid(weakPw, "user@test.de"));
    }
}
