package org.backwarden.api.adapters.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.*;
import org.backwarden.api.adapters.controller.model.converter.VaultDTOConverter;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.openapitools.api.VaultsApi;
import org.openapitools.model.VaultCreationDTO;
import org.openapitools.model.VaultDTO;
import org.openapitools.model.VaultUpdateDTO;
import org.openapitools.model.VaultWrapperDTO;

import static org.backwarden.api.adapters.controller.LinkHelper.*;
import static org.backwarden.api.adapters.controller.CacheControlHelper.*;

import java.util.List;


@ApplicationScoped
public class VaultController implements VaultsApi {

    @Inject
    VaultUseCase vaultService;

    @Inject
    SecurityIdentity identity;

    @Context
    UriInfo uriInfo;

    @Context
    Request req;


    @Override
    public Response usersUserIdVaultsVaultIdPut(Integer userId, Integer vaultId, VaultUpdateDTO vaultUpdateDTO) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        Vault vault = null;
        try {
            vault = vaultService.getVault(vaultId);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        EntityTag etag = new EntityTag(Integer.toString(vault.hashCode()));
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return builder.build();
        }
        vaultService.updateVault(vaultId, VaultDTOConverter.fromDTO(vaultUpdateDTO));
        return Response.noContent().link(getOneVault(uriInfo, userId, vaultId), relNameGetOneVault).cacheControl(notStore()).build();
    }

    @Override
    public Response usersUserIdVaultsGet(Integer userId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        VaultWrapperDTO vaultWrapperDTO = new VaultWrapperDTO();
        List<VaultDTO> vaultDTOS = VaultDTOConverter.toDTOList(vaultService.getAllVaults(userId));

        for (VaultDTO vaultDTO : vaultDTOS) {
            vaultDTO.setSelfLink(
                    uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultDTO.getId()).build());
        }

        vaultWrapperDTO.setVaultDTOS(vaultDTOS);

        vaultWrapperDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults").resolveTemplate("userid", userId).build());
        EntityTag etag = new EntityTag(Integer.toString(vaultWrapperDTO.hashCode()));
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return Response.notModified().build();
        }
        return Response.ok(vaultWrapperDTO).link(createVault(uriInfo, userId), relNameCreateVault).tag(etag).cacheControl(cachePrivateMustRevalidate()).build();
    }

    @Override
    public Response usersUserIdVaultsPost(Integer userId, VaultCreationDTO vaultCreationDTO) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        long vaultid = vaultService.createVault(userId, VaultDTOConverter.fromDTO(vaultCreationDTO));
        return Response.created(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultid).build()).cacheControl(notStore()).build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdDelete(Integer userId, Integer vaultId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        Vault vault = vaultService.getVault(vaultId);
        EntityTag etag = new EntityTag(Integer.toString(vault.hashCode()));
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return builder.build();
        }
        vaultService.deleteVault(vaultId);
        return Response.noContent().link(getAllVaults(uriInfo, userId), relNameGetAllVaults).cacheControl(notStore()).build();
    }

    @Override
    public Response usersUserIdVaultsVaultIdGet(Integer userId, Integer vaultId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }
        Vault vault = null;
        try {
            vault = vaultService.getVault(vaultId);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        VaultDTO vaultDTO = VaultDTOConverter.toDTO(vault);
        vaultDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultDTO.getId()).build());
        EntityTag etag = new EntityTag(Integer.toString(vault.hashCode()));
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return Response.notModified().build();
        }

        return Response.ok(vaultDTO).link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).link(deleteVault(uriInfo, userId, vaultId), relNameDeleteVault).link(getAllVaults(uriInfo, currentUserId), relNameGetAllVaults).link(updateVault(uriInfo, currentUserId, vaultId), relNameUpdateVault).tag(etag).cacheControl(cachePrivateMustRevalidate()).build();
    }

}
