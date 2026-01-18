package org.backwarden.api.adapters.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

import static org.backwarden.api.adapters.controller.helper.LinkHelper.*;
import static org.backwarden.api.adapters.controller.helper.CacheControlHelper.*;
import static org.backwarden.api.adapters.controller.helper.AuthenticationHelper.*;

import java.util.List;


@ApplicationScoped
public class VaultController implements VaultsApi {

    @Inject
    VaultUseCase vaultUseCase;

    @Inject
    SecurityIdentity identity;

    @Context
    UriInfo uriInfo;

    @Context
    Request req;

    @Override
    public Response createUserVault(Integer userId, VaultCreationDTO vaultCreationDTO) {
        assertUserHasAccessToResource(identity, userId);
        long vaultid = vaultUseCase.createVault(userId, VaultDTOConverter.fromDTO(vaultCreationDTO));
        return Response.created(uriInfo.getBaseUriBuilder().path("/users/{userid}/vaults/{vaultid}").resolveTemplate("userid", userId).resolveTemplate("vaultid", vaultid).build()).cacheControl(notStore()).build();
    }

    @Override
    public Response deleteUserVault(Integer userId, Integer vaultId) {
        assertUserHasAccessToResource(identity, userId);
        Vault vault = vaultUseCase.getVault(vaultId);
        EntityTag etag = new EntityTag(Integer.toString(vault.hashCode()));
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return builder.build();
        }
        vaultUseCase.deleteVault(vaultId);
        return Response.noContent().link(getAllVaults(uriInfo, userId), relNameGetAllVaults).cacheControl(notStore()).build();
    }

    @Override
    public Response getUserVaultById(Integer userId, Integer vaultId) {
        assertUserHasAccessToResource(identity, userId);
        Vault vault = null;
        try {
            vault = vaultUseCase.getVault(vaultId);
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

        return Response.ok(vaultDTO).link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).link(deleteVault(uriInfo, userId, vaultId), relNameDeleteVault).link(getAllVaults(uriInfo, userId), relNameGetAllVaults).link(updateVault(uriInfo, userId, vaultId), relNameUpdateVault).tag(etag).cacheControl(cachePrivateMustRevalidate()).build();
    }

    @Override
    public Response listUserVaults(Integer userId) {
        assertUserHasAccessToResource(identity, userId);
        VaultWrapperDTO vaultWrapperDTO = new VaultWrapperDTO();
        List<VaultDTO> vaultDTOS = VaultDTOConverter.toDTOList(vaultUseCase.getAllVaults(userId));

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
    public Response updateUserVault(Integer userId, Integer vaultId, VaultUpdateDTO vaultUpdateDTO) {
        assertUserHasAccessToResource(identity, userId);
        Vault vault = null;
        try {
            vault = vaultUseCase.getVault(vaultId);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        vaultUseCase.updateVault(vaultId, VaultDTOConverter.fromDTO(vaultUpdateDTO));
        return Response.noContent().link(getOneVault(uriInfo, userId, vaultId), relNameGetOneVault).cacheControl(notStore()).build();
    }
}
