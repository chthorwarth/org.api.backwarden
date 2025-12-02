package org.backwarden.api.adapters.database;


import org.backwarden.api.logic.model.BankAccount;
import org.backwarden.api.logic.ports.output.persistence.CredentialRepository;

import java.util.ArrayList;

public class CredentialAdapter implements CredentialRepository
{
	/* TODO better use a map here */
	private final ArrayList<BankAccountEntity> accounts = new ArrayList<>( );

	/*@Override
	public BankAccount findAccountById(long id )
	{
		BankAccountEntity entity = accounts.stream( )
										   .filter( account -> account.getId( ) == id )
										   .findFirst( )
										   .orElse( null );
		return mapToAccount( entity );
	}*/

	@Override
	public void createAccount( long accountNumber, String accountHolder, double balance )
	{
		accounts.add( new BankAccountEntity( accountNumber, accountHolder, balance ) );
	}

	/*@Override
	public boolean updateAccount( long id, BankAccount newAccount )
	{
		BankAccount oldAccount = findAccountById( id );

		if ( oldAccount == null )
			return false;

		accounts.set( ( int ) oldAccount.getId( ), mapToEntity( newAccount ) );
		return true;
	}*/

	private BankAccountEntity mapToEntity( BankAccount account )
	{
		if ( account == null )
			return null;
		return new BankAccountEntity( account.getId( ), account.getAccountHolder( ), account.getBalance( ) );
	}

	private BankAccount mapToAccount( BankAccountEntity entity )
	{
		if ( entity == null )
			return null;
		return new BankAccount( entity.getId( ), entity.getAccountHolder( ), entity.getBalance( ) );
	}
}
