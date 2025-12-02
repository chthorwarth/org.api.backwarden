package org.backwarden.api.adapters.database;
import jakarta.enterprise.event.Observes;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.model.BankAccount;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

@ApplicationScoped

public class CredentialAdapter implements CredentialRepository {
    @Inject
    EntityManager entityManager;

    @Transactional // Wichtig für Schreiboperationen
    @Override
    public void saveCredential(Credential credential) {
        entityManager.persist(new CredentialEntity());
    }
    public Credential getCredential(long id) {
        CredentialEntity ce = entityManager.find(CredentialEntity.class, id);
        return new Credential();
    }
}
