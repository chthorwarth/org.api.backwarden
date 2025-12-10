package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.backwarden.api.adapters.database.VaultAdapter;
import org.backwarden.api.adapters.database.model.VaultEntity;

@Path("/vault")
@ApplicationScoped
public class VaultController
{
    @Inject
    VaultAdapter vaultAdapter;

	/*private final CredentialAPI credentialService;

	public CredentialController(CredentialAPI credentialService) {
		this.credentialService = credentialService;
	}

	public void createCredentials(String username, String password) {
		credentialService.createCredentials(username, password);
	}*/

    @GET
    public String hello()
    {
        System.out.println("Hello World");
        VaultEntity vault = new VaultEntity();
        vault.setTitle("Simon");
        vaultAdapter.saveVault(vault);

        //VaultEntity vaultNew = vaultAdapter.getVault(1);
        //System.out.println(vaultNew.getTitle());

        return "hello";

    }
}
