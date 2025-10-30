package org.example.logic.ports.output.persistence;

import org.example.logic.model.BankAccount;

public interface CredentialRepository
{
	void createAccount( long accountNumber, String accountHolder, double balance );

}
