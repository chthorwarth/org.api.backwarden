package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import org.openapitools.api.VaultsApi;
import org.openapitools.model.VaultDTO;


@ApplicationScoped
@Path("/")
public class VaultController implements VaultsApi {

    @Override
    public void usersUserIdVaultsPost(Integer userId, VaultDTO vaultDTO) {
        System.out.println(vaultDTO.getTitle());
    }

    @Override
    public VaultDTO usersUserIdVaultsVaultIdGet(Integer userId, Integer vaultId) {
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.id(1L);
        vaultDTO.setTitle("TestVault");
        return vaultDTO;
    }

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


}
