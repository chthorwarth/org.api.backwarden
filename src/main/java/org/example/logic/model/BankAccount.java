package org.example.logic.model;

public class BankAccount
{
	private long id = 0;
	private final String accountHolder;
	private double balance;

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

	public BankAccount( long accountNumber, String accountHolder, double balance )
	{
		this.id = accountNumber;
		this.accountHolder = accountHolder;
		this.balance = balance;
	}

	public void decreaseBalance( double amount )
	{
		if ( amount > 0 && balance >= amount )
		{
			balance = balance - amount;
			return;
		}
		throw new RuntimeException( "Insufficient balance" );
	}

	public void increaseBalance( double amount )
	{
		if ( amount > 0 )
		{
			balance = balance + amount;
			return;
		}
		throw new RuntimeException( "Amount not set up correctly" );
	}

	@Override
	public String toString( )
	{
		return "ID: " + id + " Holder: " + accountHolder + " Balance: " + balance;
	}
}
