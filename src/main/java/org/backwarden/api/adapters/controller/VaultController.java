package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.backwarden.api.adapters.controller.model.converter.VaultDTOConverter;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.openapitools.api.VaultsApi;

import org.openapitools.model.VaultDTO;
import org.openapitools.model.VaultWrapperDTO;

import java.net.URI;
import java.util.List;


@ApplicationScoped
public class VaultController implements VaultsApi {

    @Inject
    VaultUseCase vaultService;

    @Override
    public Response usersUserIdVaultsGet(Integer userId) {
        VaultWrapperDTO vaultWrapperDTO = new VaultWrapperDTO();
        vaultWrapperDTO.setSelfLink(URI.create("/users/" + userId + "/vaults"));

        List<VaultDTO> vaultDTOS = VaultDTOConverter.toDTOList(vaultService.getAllVaults(userId));

        //Do we set the self-link here or in the converter?

        for (VaultDTO vaultDTO : vaultDTOS)
        {
            vaultDTO.setSelfLink(URI.create("/users/" + userId + "/vaults/" + vaultDTO.getId()));
        }

        vaultWrapperDTO.setVaultDTOS(vaultDTOS);
        return Response.ok(vaultWrapperDTO).build();
    }

    @Override
    public Response usersUserIdVaultsPost(Integer userId, VaultDTO vaultDTO) {
        vaultService.createVault(userId,VaultDTOConverter.fromDTO(vaultDTO));
        return Response.ok().build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdDelete(Integer userId, Integer vaultId) {
        vaultService.deleteVault(vaultId);
        return Response.ok().build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdPut(Integer userId, Integer vaultId, VaultDTO vaultDTO) {
        vaultService.updateVault(vaultId, VaultDTOConverter.fromDTO(vaultDTO));
        vaultDTO.setSelfLink( URI.create("/users/" + userId + "/vaults/" + vaultId));

        return Response.ok(vaultDTO).build();
    }

}
