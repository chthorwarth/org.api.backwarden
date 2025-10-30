package org.example.adapters.database;

public class BankAccountEntity
{
	private long id = 0;
	private final String accountHolder;

	public double getBalance( )
	{
		return balance;
	}

	public String getAccountHolder( )
	{
		return accountHolder;
	}

	public long getId( )
	{
		return id;
	}

	private double balance;

	public BankAccountEntity( long accountNumber, String accountHolder, double balance )
	{
		this.id = accountNumber;
		this.accountHolder = accountHolder;
		this.balance = balance;
	}

	@Override
	public String toString( )
	{
		return "ID: " + id + " Holder: " + accountHolder + " Balance: " + balance;
	}
}
