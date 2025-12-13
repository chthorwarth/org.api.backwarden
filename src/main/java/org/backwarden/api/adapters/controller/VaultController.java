package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.backwarden.api.adapters.controller.model.UserDTO;
import org.backwarden.api.adapters.controller.model.VaultDTO;
import org.backwarden.api.adapters.controller.model.interfaces.VaultApi;
import org.backwarden.api.adapters.database.VaultAdapter;
import org.backwarden.api.adapters.database.model.VaultEntity;

@ApplicationScoped
public class VaultController implements VaultApi
{
//    @Inject
//    VaultAdapter vaultAdapter;
//
//	/*private final CredentialAPI credentialService;
//
//	public CredentialController(CredentialAPI credentialService) {
//		this.credentialService = credentialService;
//	}
//
//	public void createCredentials(String username, String password) {
//		credentialService.createCredentials(username, password);
//	}*/
//
//    @GET
//    public String hello()
//    {
//        System.out.println("Hello World");
//        VaultEntity vault = new VaultEntity();
//        vault.setTitle("Simon");
//        vaultAdapter.saveVault(vault);
//
//        //VaultEntity vaultNew = vaultAdapter.getVault(1);
//        //System.out.println(vaultNew.getTitle());
//
//        return "hello";
//
//    }

    @Override
    public void vaultsPost(VaultDTO vaultDTO)
    {
        System.out.println(vaultDTO.getTitle());
    }

    @Override
    public VaultDTO vaultsVaultIdGet(Integer vaultId)
    {
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.setId(1);
        vaultDTO.setTitle("TestVault");
        return vaultDTO;
    }
}
