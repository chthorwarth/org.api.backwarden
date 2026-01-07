package org.backwarden.api.logic.services;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class CryptoHelperTest {

    private SecretKey deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100_000, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();
        return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    }

    @Test
    void encryptAndDecrypt_ShouldReturnOriginalText() throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        SecretKey key = deriveKey("MasterPasswort123!", salt);

        String plaintext = "meinGeheimesPasswort!";

        CryptoHelper.Encrypted encrypted = CryptoHelper.encrypt(plaintext, key);

        String decrypted = CryptoHelper.decrypt(
                encrypted.iv(),
                encrypted.ciphertext(),
                key
        );

        assertEquals(plaintext, decrypted);
    }

    @Test
    void encrypt_ShouldProduceDifferentCiphertextEachTime() throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        SecretKey key = deriveKey("MasterPasswort123!", salt);

        String plaintext = "gleichesPasswort";

        CryptoHelper.Encrypted e1 = CryptoHelper.encrypt(plaintext, key);
        CryptoHelper.Encrypted e2 = CryptoHelper.encrypt(plaintext, key);

        assertNotEquals(e1.ciphertext(), e2.ciphertext()); // wegen unterschiedlichem IV
        assertNotEquals(e1.iv(), e2.iv());
    }

    @Test
    void decrypt_WithWrongKey_ShouldFail() throws Exception {

        byte[] salt1 = new byte[16];
        byte[] salt2 = new byte[16];
        new SecureRandom().nextBytes(salt1);
        new SecureRandom().nextBytes(salt2);

        SecretKey correctKey = deriveKey("MasterPasswort123!", salt1);
        SecretKey wrongKey = deriveKey("MasterPasswort123!", salt2);

        CryptoHelper.Encrypted encrypted =
                CryptoHelper.encrypt("secret", correctKey);

        assertThrows(Exception.class, () ->
                CryptoHelper.decrypt(
                        encrypted.iv(),
                        encrypted.ciphertext(),
                        wrongKey
                )
        );
    }

    @Test
    void decrypt_WithModifiedCiphertext_ShouldFail() throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        SecretKey key = deriveKey("MasterPasswort123!", salt);

        CryptoHelper.Encrypted encrypted =
                CryptoHelper.encrypt("secret", key);

        // Ciphertext manipulieren
        byte[] cipher = Base64.getDecoder().decode(encrypted.ciphertext());
        cipher[0] ^= 0x01; // ein Bit flippen
        String modifiedCipher = Base64.getEncoder().encodeToString(cipher);

        assertThrows(Exception.class, () ->
                CryptoHelper.decrypt(
                        encrypted.iv(),
                        modifiedCipher,
                        key
                )
        );
    }
}
