package org.backwarden.api.logic.ports.output.persistence;

public interface CredentialRepository
{
	void createAccount( long accountNumber, String accountHolder, double balance );

}
