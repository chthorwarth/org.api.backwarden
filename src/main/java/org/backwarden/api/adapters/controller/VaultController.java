package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import org.openapitools.api.VaultsApi;
import org.openapitools.model.VaultDTO;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
@Path("/")
public class VaultController implements VaultsApi {

    @Override
    public List<VaultDTO> usersUserIdVaultsGet(Integer userId) {
        List<VaultDTO> vaultDTOS = new ArrayList<>();
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.id(1L);
        vaultDTO.setTitle("TestVault");
        vaultDTOS.add(vaultDTO);
        return vaultDTOS;
    }

    @Override
    public void usersUserIdVaultsPost(Integer userId, VaultDTO vaultDTO) {
        System.out.println(vaultDTO.getTitle());
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
