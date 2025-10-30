package org.example;

import org.example.adapters.api.CredentialController;
import org.example.adapters.database.CredentialAdapter;
import org.example.logic.ports.input.CredentialAPI;
import org.example.logic.ports.output.persistence.CredentialRepository;
import org.example.logic.services.CredentialService;

public class Main
{
	static void main( )
	{
		CredentialRepository repository = new CredentialAdapter();
		CredentialAPI api = new CredentialService(repository);
		CredentialController controller = new CredentialController(api);
	}
}
