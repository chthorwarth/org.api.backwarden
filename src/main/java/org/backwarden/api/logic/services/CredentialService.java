package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialUseCase;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.List;


@ApplicationScoped
public class CredentialService implements CredentialUseCase {
    private static final Logger log = LoggerFactory.getLogger(CredentialService.class);
    @Inject
    CredentialRepository credentialRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    SessionKeyStore sessionKeyStore;

    @Override
    public long createCredentials(Credential credential, long vaultId) {
        String sessionId = jwt.getClaim("sid");
        SecretKey secretKey = sessionKeyStore.get(sessionId);
        encrypt(credential, secretKey);
        credential.setPassword(null);
        Credential credential1 = credentialRepository.saveCredential(credential, vaultId);
        return credential1.getId();
    }

    @Override
    public Credential getCredential(long id) {
        Credential credential = credentialRepository.getCredential(id);
        String sessionId = jwt.getClaim("sid");
        SecretKey secretKey = sessionKeyStore.get(sessionId);
        decryptCredential(credential, secretKey);
        credential.setPasswordSecure(ValidationHelper.isPasswordValid(credential.getPassword(), credential.getUsername()));
        return credential;
    }

    @Override
    public List<Credential> getAllCredentials(long vaultId, String title, int page, int size) {
        String sessionId = jwt.getClaim("sid");
        SecretKey secretKey = sessionKeyStore.get(sessionId);
        List<Credential> credentials =
                credentialRepository.getAllCredentials(vaultId, title, page, size);
        for (Credential credential : credentials) {
            decryptCredential(credential, secretKey);
        }
        return credentials;
    }

    void decryptCredential(Credential credential, SecretKey secretKey) {
        if (secretKey == null) {
            throw new SecurityException("Session expired — login again");
        }
        try {
            String password = CryptoHelper.decrypt(
                    credential.getPasswordIV(),
                    credential.getPasswordCiphertext(),
                    secretKey
            );

            credential.setPassword(password);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CryptionGoneWrongException("Decryption of credential password failed");
        }
    }

    @Override
    @Transactional
    public void deleteCredential(long id) {
        credentialRepository.deleteCredential(id);
    }

    @Override
    public void updateCredential(long id, Credential credential) {
        String sessionId = jwt.getClaim("sid");
        SecretKey secretKey = sessionKeyStore.get(sessionId);
        encrypt(credential, secretKey);
        credential.setPassword(null);
        credentialRepository.updateCredential(id, credential);
    }

    private void encrypt(Credential credential, SecretKey secretKey) {
        if (secretKey == null) {
            throw new SecurityException("Session expired — login again");
        }
        CryptoHelper.Encrypted encrypted = null;
        try {
            encrypted = CryptoHelper.encrypt(credential.getPassword(), secretKey);
        } catch (Exception e) {
            throw new CryptionGoneWrongException("Unkown Error while encrypting credential password");
        }
        credential.setPasswordCiphertext(encrypted.ciphertext());
        credential.setPasswordIV(encrypted.iv());
    }

    @Override
    public long countCredentials(long vaultId, String title) {
        return credentialRepository.countCredentials(vaultId, title);
    }
}
