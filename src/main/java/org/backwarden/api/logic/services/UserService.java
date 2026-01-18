package org.backwarden.api.logic.services;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.backwarden.api.logic.JwtKeyGenerator;
import org.backwarden.api.logic.exceptions.DomainValidationException;
import org.backwarden.api.logic.exceptions.UserNotFoundException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

import io.quarkus.elytron.security.common.BcryptUtil;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@ApplicationScoped
public class UserService implements UserUseCase {


    @Inject
    UserRepository userRepository;

    @Inject
    SessionKeyStore sessionKeyStore;

    String generateKDFSalt() {
        byte[] kdfSalt = new byte[16]; // 128 Bit
        SecureRandom random = new SecureRandom();
        random.nextBytes(kdfSalt);

        return Base64.getEncoder().encodeToString(kdfSalt);
    }

    SecretKey generateKDF(String masterPassword, String base64kdfSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] passwordChars = masterPassword.toCharArray();
        byte[] salt = Base64.getDecoder().decode(base64kdfSalt);

        PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, 100_000, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }

    String getPasswordHash(String password) {
        return BcryptUtil.bcryptHash(password);
    }


    @Override
    public long createUser(User user) {
        if (ValidationHelper.isMailValid(user.getMasterEmail()) && ValidationHelper.isPasswordValid(user.getMasterPassword(), user.getMasterEmail())) {
            String passwordHash = getPasswordHash(user.getMasterPassword());

            String kdfSalt = generateKDFSalt();
            user.setMasterPasswordSalt(kdfSalt);

            user.setMasterPasswordHash(passwordHash); // Only save the hashed pw
            return userRepository.saveUser(user).getId();
        } else {
            throw new DomainValidationException("Invalid email or password");
        }

    }

    @Override
    public User getUser(long id) {
        return userRepository.getUser(id);
    }

    @Override
    public User authenticate(String mail, String password) {
        User user = userRepository.getUser(mail);
        if (mail.equals(user.getMasterEmail()) && BcryptUtil.matches(password, user.getMasterPasswordHash())) {
            return user;
        }
        throw new UserNotFoundException("Can't find user for given String mail");
    }

    public String generateJWTandKDF(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKey kdf = generateKDF(password, user.getMasterPasswordSalt());
        String sessionId = UUID.randomUUID().toString();
        String jwtToken = generateJWTtoken(user, sessionId);
        sessionKeyStore.put(sessionId, kdf);
        return jwtToken;
    }

    private String generateJWTtoken(User user, String sessionId) {
        //.upn(user.getMasterEmail())
        return Jwt.issuer("http://org.backwarden.api")
                //.upn(user.getMasterEmail())
                .subject(String.valueOf(user.getId()))
                .claim("sid", sessionId)
                .groups(Set.of("user"))
                .expiresIn(Duration.ofHours(2))
                .sign(JwtKeyGenerator.PRIVATE_KEY);
    }
}


