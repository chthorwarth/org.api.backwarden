package org.backwarden.api.logic.services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoHelper {


    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;         // 96-bit IV
    private static final int TAG_LENGTH = 128;       // auth-tag bits

    public static Encrypted encrypt(String plaintext, SecretKey key) throws Exception {

        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] ciphertext = cipher.doFinal(
                plaintext.getBytes(StandardCharsets.UTF_8));

        return new Encrypted(
                Base64.getEncoder().encodeToString(iv),
                Base64.getEncoder().encodeToString(ciphertext)
        );
    }

    public static String decrypt(String ivBase64, String cipherBase64, SecretKey key) throws Exception {

        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] ciphertext = Base64.getDecoder().decode(cipherBase64);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plain = cipher.doFinal(ciphertext);

        return new String(plain, StandardCharsets.UTF_8);
    }

    public record Encrypted(String iv, String ciphertext) {
    }
}
