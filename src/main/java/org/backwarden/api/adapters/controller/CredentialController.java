package org.backwarden.api.adapters.controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.backwarden.api.adapters.controller.model.converter.CredentialDTOConverter;
import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.ports.input.CredentialUseCase;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialWrapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@ApplicationScoped
public class CredentialController implements CredentialsApi {

    private static final Logger log = LoggerFactory.getLogger(CredentialController.class);
    @Inject
    CredentialUseCase credentialService;

    @Inject
    SecurityIdentity identity;

    @Inject
    VaultUseCase vaultService;

    @Context
    UriInfo uriInfo;

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdDelete(Integer vaultId, Integer credentialId) {
        try {
            long currentUserId = Long.parseLong(identity.getPrincipal().getName());
            long userId = vaultService.getUserIdByVaultId(vaultId);
            if (currentUserId != userId) {
                throw new ForbiddenException("Not your account");
            }
            credentialService.deleteCredential(credentialId);
            URI getAllCredentials = uriInfo
                    .getBaseUriBuilder()
                    .path("/vaults/{vaultid}")
                    .resolveTemplate("vaultid", vaultId)
                    .build();
            return Response.noContent().link(getAllCredentials, "getAllCredentials").build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SecurityException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdGet(Integer vaultId, Integer credentialId) {
        try {
            long currentUserId = Long.parseLong(identity.getPrincipal().getName());
            long userId = vaultService.getUserIdByVaultId(vaultId);
            if (currentUserId != userId) {
                throw new ForbiddenException("Not your account");
            }
            CredentialDTO credentialDTO = CredentialDTOConverter.toDTO(credentialService.getCredential(credentialId));
            credentialDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/vaults/{vaultid}/credentials/{credentialid}").resolveTemplate("credentialid", credentialId).resolveTemplate("vaultid", vaultId).build());
            URI vaultsDelete = uriInfo
                    .getBaseUriBuilder()
                    .path("vaults/{vaultid}/credentials/{credentialid}")
                    .resolveTemplate("vaultid", vaultId)
                    .resolveTemplate("credentialid", credentialDTO.getId())
                    .build();
            return Response.ok(credentialDTO).link(vaultsDelete, "deleteVault").build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SecurityException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response vaultsVaultIdCredentialsGet(Integer vaultId) {
        try {
            long currentUserId = Long.parseLong(identity.getPrincipal().getName());
            long userId = vaultService.getUserIdByVaultId(vaultId);
            if (currentUserId != userId) {
                throw new ForbiddenException("Not your account");
            }
            CredentialWrapperDTO wrapperDTO = new CredentialWrapperDTO();
            wrapperDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/vaults/{vaultid}/credentials").resolveTemplate("vaultid", vaultId).build());

            List<CredentialDTO> credentialDTOs = CredentialDTOConverter.toDTOList(credentialService.getAllCredentials(vaultId));

            for (CredentialDTO dto : credentialDTOs)
                dto.setSelfLink(uriInfo.getBaseUriBuilder().path("/vaults/{vaultid}/credentials/{credentialid}").resolveTemplate("vaultid", vaultId).resolveTemplate("credentialid", dto.getId()).build());


            wrapperDTO.credentialDTOS(credentialDTOs);
            URI credentialsCreate = uriInfo
                    .getBaseUriBuilder()
                    .path("vaults/{vaultid}/credentials")
                    .resolveTemplate("vaultid", vaultId)
                    .build();
            return Response.ok(wrapperDTO).link(credentialsCreate, "createCredential").build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response vaultsVaultIdCredentialsPost(Integer vaultId, CredentialCreationDTO credentialCreationDTO) {
        try {
            long currentUserId = Long.parseLong(identity.getPrincipal().getName());
            long userId = vaultService.getUserIdByVaultId(vaultId);
            if (currentUserId != userId) {
                throw new ForbiddenException("Not your account");
            }
            long credentialid = credentialService.createCredentials(CredentialDTOConverter.fromDTO(credentialCreationDTO), vaultId);
            return Response.created(uriInfo.getBaseUriBuilder().path("/vaults/{vaultid}/credentials/{credentialid}").resolveTemplate("credentialid", credentialid).resolveTemplate("vaultid", vaultId).build()).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}