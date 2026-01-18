package org.backwarden.api.logic.services;

import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.model.Credential;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.model.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.model.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CredentialServiceTest {

    private CredentialService service;

    @BeforeEach
    void setUp() {
        service = new CredentialService();
    }

    @Test
    void encrypt_thenDecrypt_roundtrip_restoresOriginalPassword() throws Exception {
        SecretKey key = newAesKey();

        Credential c = new Credential();
        c.setUsername("alice");
        c.setPassword("S3cr3t-Password!");

        invokeEncrypt(service, c, key);

        assertNotNull(c.getPasswordIV(), "IV must be set");
        assertNotNull(c.getPasswordCiphertext(), "Ciphertext must be set");
        assertFalse(c.getPasswordIV().isBlank(), "IV must not be blank");
        assertFalse(c.getPasswordCiphertext().isBlank(), "Ciphertext must not be blank");

        // decryptCredential is package-private -> direct call (same package)
        service.decryptCredential(c, key);

        assertEquals("S3cr3t-Password!", c.getPassword(), "Password must be restored after decryption");
    }

    @Test
    void decryptCredential_nullKey_throwsSecurityException() {
        Credential c = new Credential();
        c.setPasswordIV(base64RandomBytes(12));
        c.setPasswordCiphertext(base64RandomBytes(64));

        SecurityException ex = assertThrows(SecurityException.class,
                () -> service.decryptCredential(c, null));

        assertTrue(ex.getMessage().contains("Session expired"), "Expected session expired message");
    }

    @Test
    void encrypt_nullKey_throwsSecurityException() {
        Credential c = new Credential();
        c.setPassword("pw");

        SecurityException ex = assertThrows(SecurityException.class,
                () -> invokeEncrypt(service, c, null));

        assertTrue(ex.getMessage().contains("Session expired"), "Expected session expired message");
    }

    @Test
    void decryptCredential_invalidCiphertextOrIv_throwsCryptionGoneWrongException() throws Exception {
        SecretKey key = newAesKey();

        Credential c = new Credential();
        // valid Base64, but "wrong" bytes in context -> decrypt should fail
        c.setPasswordIV(base64RandomBytes(12));
        c.setPasswordCiphertext(base64RandomBytes(64));

        assertThrows(CryptionGoneWrongException.class,
                () -> service.decryptCredential(c, key));
    }

    @Test
    void decryptCredential_nonBase64Input_throwsCryptionGoneWrongException() throws Exception {
        SecretKey key = newAesKey();

        Credential c = new Credential();
        c.setPasswordIV("###not-base64###");
        c.setPasswordCiphertext("###not-base64###");

        assertThrows(CryptionGoneWrongException.class,
                () -> service.decryptCredential(c, key));
    }

    // ---------------- helpers ----------------

    private static SecretKey newAesKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        return kg.generateKey();
    }

    private static String base64RandomBytes(int n) {
        byte[] b = new byte[n];
        new SecureRandom().nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    private static void invokeEncrypt(CredentialService svc, Credential c, SecretKey key) throws Exception {
        Method m = CredentialService.class.getDeclaredMethod("encrypt", Credential.class, SecretKey.class);
        m.setAccessible(true);

        try {
            m.invoke(svc, c, key);
        } catch (InvocationTargetException ite) {
            // unwrap for assertThrows & clearer failures
            Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException re) throw re;
            if (cause instanceof Error err) throw err;
            throw ite;
        }
    }
}