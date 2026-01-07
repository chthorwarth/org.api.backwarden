package org.backwarden.api.adapters.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.backwarden.api.adapters.controller.model.converter.VaultDTOConverter;
import org.backwarden.api.adapters.database.model.VaultEntity;
import org.backwarden.api.adapters.database.model.converter.VaultEntityConverter;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.openapitools.api.VaultsApi;
import org.openapitools.model.VaultCreationDTO;
import org.openapitools.model.VaultDTO;
import org.openapitools.model.VaultUpdateDTO;
import org.openapitools.model.VaultWrapperDTO;

import java.net.URI;
import java.util.List;


@ApplicationScoped
public class VaultController implements VaultsApi {

    @Inject
    VaultUseCase vaultService;

    @Inject
    SecurityIdentity identity;

    @Context
    UriInfo uriInfo;


    @Override
    public Response usersUserIdVaultsGet(Integer userId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        VaultWrapperDTO vaultWrapperDTO = new VaultWrapperDTO();
        vaultWrapperDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults").resolveTemplate("userid", userId).build());
        //vaultWrapperDTO.setSelfLink(URI.create("/users/" + userId + "/vaults"));

        List<VaultDTO> vaultDTOS = VaultDTOConverter.toDTOList(vaultService.getAllVaults(userId));

        for (VaultDTO vaultDTO : vaultDTOS) {
            vaultDTO.setSelfLink(
                    uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultDTO.getId()).build());
            //URI.create(uriInfo.getAbsolutePathBuilder() + "/users/" + userId + "/vaults/" + vaultDTO.getId()));
        }

        URI vaultsCreate = uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults")
                .resolveTemplate("userid", currentUserId)
                .build();
        vaultWrapperDTO.setVaultDTOS(vaultDTOS);
        return Response.ok(vaultWrapperDTO).link(vaultsCreate, "createVault").build();
    }

    @Override
    public Response usersUserIdVaultsPost(Integer userId, VaultCreationDTO vaultCreationDTO) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        long vaultid = vaultService.createVault(userId, VaultDTOConverter.fromDTO(vaultCreationDTO));
        return Response.created(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultid).build()).build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdDelete(Integer userId, Integer vaultId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        URI getAllVaults = uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults")
                .resolveTemplate("userid", currentUserId)
                .build();
        vaultService.deleteVault(vaultId);
        return Response.noContent().link(getAllVaults, "getAllVaults").build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdGet(Integer userId, Integer vaultId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        VaultDTO vaultDTO = VaultDTOConverter.toDTO(vaultService.getVault(vaultId));
        vaultDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultDTO.getId()).build());

        URI credentialsGet = uriInfo
                .getBaseUriBuilder()
                .path("vaults/{vaultid}/credentials")
                .resolveTemplate("vaultid", vaultDTO.getId())
                .build();
        URI deleteVault = uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults/{vaultid}")
                .resolveTemplate("userid", currentUserId)
                .resolveTemplate("vaultid", vaultDTO.getId())
                .build();
        return Response.ok(vaultDTO).link(credentialsGet, "getAllCredentials").link(deleteVault, "deleteVault").build();
    }

}
