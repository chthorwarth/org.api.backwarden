package org.backwarden.api.logic;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Startup
@ApplicationScoped
public class JwtKeyGenerator {

    public static RSAPrivateKey PRIVATE_KEY;
    public static RSAPublicKey PUBLIC_KEY;

    public JwtKeyGenerator() throws Exception {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        PRIVATE_KEY = (RSAPrivateKey) pair.getPrivate();
        PUBLIC_KEY = (RSAPublicKey) pair.getPublic();

        try (FileOutputStream fos = new FileOutputStream("publicKey.pem")) {
            fos.write(toPem("PUBLIC KEY", PUBLIC_KEY.getEncoded()).getBytes(StandardCharsets.UTF_8));
        }
    }

    private static String toPem(String title, byte[] bytes) {
        return "-----BEGIN " + title + "-----\n"
                + Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(bytes)
                + "\n-----END " + title + "-----\n";
    }
}
