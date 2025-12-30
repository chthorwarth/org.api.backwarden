package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.openapitools.api.VaultsApi;
import org.openapitools.model.VaultCreationDTO;
import org.openapitools.model.VaultDTO;
import org.openapitools.model.VaultUpdateDTO;
import org.openapitools.model.VaultWrapperDTO;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class VaultController implements VaultsApi {

    @Override
    public Response usersUserIdVaultsGet(Integer userId) {
        VaultWrapperDTO vaultWrapperDTO = new VaultWrapperDTO();
        vaultWrapperDTO.setSelfLink(URI.create("/users/" + userId + "/vaults"));
        List<VaultDTO> vaultDTOS = new ArrayList<>();
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.id(1L);
        vaultDTO.setTitle("TestVault");
        vaultDTOS.add(vaultDTO);
        vaultWrapperDTO.setVaultDTOS(vaultDTOS);
        return Response.ok(vaultWrapperDTO).build();
    }

    @Override
    public Response usersUserIdVaultsPost(Integer userId, VaultCreationDTO vaultCreationDTO) {
        return Response.ok().build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdDelete(Integer userId, Integer vaultId) {
        System.out.println("Vault deleted: " + vaultId);
        return Response.ok().build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdPut(Integer userId, Integer vaultId, VaultUpdateDTO vaultUpdateDTO) {
        VaultDTO vaultDTO = new VaultDTO();
        vaultDTO.setSelfLink(URI.create("/users/" + userId + "/vaults/" + vaultId));
        return Response.ok(vaultDTO).build();
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
