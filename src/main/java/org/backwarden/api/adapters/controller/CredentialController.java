package org.backwarden.api.adapters.controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.*;
import org.backwarden.api.adapters.controller.model.converter.CredentialDTOConverter;
import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.Vault;
import org.backwarden.api.logic.ports.input.CredentialUseCase;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialUpdateDTO;
import org.openapitools.model.CredentialWrapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;

import static org.backwarden.api.adapters.controller.LinkHelper.*;
import static org.backwarden.api.adapters.controller.CacheControlHelper.*;

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

    @Context
    Request req;

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdDelete(Integer vaultId, Integer credentialId) {
        try {
            long currentUserId = Long.parseLong(identity.getPrincipal().getName());
            long userId = vaultService.getUserIdByVaultId(vaultId);
            if (currentUserId != userId) {
                throw new ForbiddenException("Not your account");
            }
            Credential credential = credentialService.getCredential(credentialId);
            EntityTag etag = new EntityTag(Integer.toString(credential.hashCode()));
            Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
            if (builder != null) {
                return builder.build();
            }
            credentialService.deleteCredential(credentialId);
            return Response.noContent().link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).cacheControl(notStore()).build();
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
            Credential credential = credentialService.getCredential(credentialId);
            CredentialDTO credentialDTO = CredentialDTOConverter.toDTO(credential);
            credentialDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/vaults/{vaultid}/credentials/{credentialid}").resolveTemplate("credentialid", credentialId).resolveTemplate("vaultid", vaultId).build());
            EntityTag etag = new EntityTag(Integer.toString(credential.hashCode()));
            Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
            if (builder != null) {
                return Response.notModified().build();
            }
            return Response.ok(credentialDTO).link(deleteCredential(uriInfo, vaultId, credentialId), relNameDeleteCredential).link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).cacheControl(notStore()).tag(etag).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SecurityException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response vaultsVaultIdCredentialsCredentialIdPut(Integer vaultId, Integer credentialId, CredentialUpdateDTO credentialUpdateDTO) {
        return null;
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
                dto.setSelfLink(credentialLocation(uriInfo, vaultId, dto.getId()));
            wrapperDTO.credentialDTOS(credentialDTOs);

            EntityTag etag = new EntityTag(Integer.toString(wrapperDTO.hashCode()));
            Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
            if (builder != null) {
                return Response.notModified().build();
            }

            return Response.ok(wrapperDTO).link(createCredentials(uriInfo, vaultId), relNameCreateCredentials).link(getOneVault(uriInfo, userId, vaultId), relNameGetOneVault).tag(etag).cacheControl(notStore()).build();
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
            return Response.created(credentialLocation(uriInfo, vaultId, credentialid)).cacheControl(notStore()).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}