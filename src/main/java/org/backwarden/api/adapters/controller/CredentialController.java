package org.backwarden.api.adapters.controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.backwarden.api.adapters.controller.model.converter.CredentialDTOConverter;
import org.backwarden.api.logic.exceptions.CryptionGoneWrongException;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.ports.input.CredentialUseCase;
import org.backwarden.api.logic.ports.input.VaultUseCase;
import org.openapitools.api.CredentialsApi;
import org.openapitools.model.CredentialCreationDTO;
import org.openapitools.model.CredentialDTO;
import org.openapitools.model.CredentialUpdateDTO;
import org.openapitools.model.CredentialWrapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import static org.backwarden.api.adapters.controller.helper.CacheControlHelper.notStore;
import static org.backwarden.api.adapters.controller.helper.LinkHelper.*;
import static org.backwarden.api.adapters.controller.helper.AuthenticationHelper.*;

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
    public Response createVaultCredential(Integer vaultId, CredentialCreationDTO credentialCreationDTO) {
        try {
            long userId = vaultService.getUserIdByVaultId(vaultId);
            assertUserHasAccessToResource(identity, userId);
            long credentialid = credentialService.createCredentials(CredentialDTOConverter.fromDTO(credentialCreationDTO), vaultId);
            return Response.created(getOneCredential(uriInfo, vaultId, credentialid)).cacheControl(notStore()).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response deleteVaultCredential(Integer vaultId, Integer credentialId) {
        try {
            long userId = vaultService.getUserIdByVaultId(vaultId);
            assertUserHasAccessToResource(identity, userId);
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
    public Response getVaultCredentialById(Integer vaultId, Integer credentialId) {
        try {
            long userId = vaultService.getUserIdByVaultId(vaultId);
            assertUserHasAccessToResource(identity, userId);
            Credential credential = credentialService.getCredential(credentialId);
            CredentialDTO credentialDTO = CredentialDTOConverter.toDTO(credential);
            credentialDTO.setSelfLink(uriInfo.getBaseUriBuilder().path("/vaults/{vaultid}/credentials/{credentialid}").resolveTemplate("credentialid", credentialId).resolveTemplate("vaultid", vaultId).build());
            EntityTag etag = new EntityTag(Integer.toString(credential.hashCode()));
            Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
            if (builder != null) {
                return Response.notModified().build();
            }
            return Response.ok(credentialDTO).link(updateCredential(uriInfo, vaultId, credentialId), relNameUpdateCredential).link(deleteCredential(uriInfo, vaultId, credentialId), relNameDeleteCredential).link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).cacheControl(notStore()).tag(etag).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SecurityException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response listVaultCredentials(Integer vaultId, String title) {
        try {
            long userId = vaultService.getUserIdByVaultId(vaultId);
            assertUserHasAccessToResource(identity, userId);

            int page = 0;
            int size = 10;

            String pageParam = uriInfo.getQueryParameters().getFirst("page");
            String sizeParam = uriInfo.getQueryParameters().getFirst("size");

            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {
                }    // ignored? -> 400
            }
            if (sizeParam != null) {
                try {
                    size = Integer.parseInt(sizeParam);
                } catch (NumberFormatException ignored) {
                }
            }

            long totalCount = credentialService.countCredentials(vaultId, title);

            CredentialWrapperDTO wrapperDTO = new CredentialWrapperDTO();

            List<CredentialDTO> credentialDTOs = CredentialDTOConverter.toDTOList(credentialService.getAllCredentials(vaultId, title, page, size));

            for (CredentialDTO dto : credentialDTOs)
                dto.setSelfLink(getOneCredential(uriInfo, vaultId, dto.getId()));
            wrapperDTO.credentialDTOS(credentialDTOs);
            wrapperDTO.setSelfLink(createPaginationUri(uriInfo, page, size, vaultId));

            URI selfUri = createPaginationUri(uriInfo, page, size, vaultId);
            URI nextUri = createPaginationUri(uriInfo, page + 1, size, vaultId);
            URI prevUri = page > 0 ? createPaginationUri(uriInfo, page - 1, size, vaultId) : null;

            URI credentialsCreate = uriInfo.getBaseUriBuilder()
                    .path("vaults/{vaultid}/credentials")
                    .resolveTemplate("vaultid", vaultId)
                    .build();

            Response.ResponseBuilder response = Response.ok(wrapperDTO);

            response.link(selfUri, "self");
            if ((long) (page + 1) * size < totalCount) {
                response.link(nextUri, "next");
            }
            if (prevUri != null) {
                response.link(prevUri, "prev");
            }
            response.link(credentialsCreate, relNameCreateCredentials);
            response.link(getOneVault(uriInfo, userId, vaultId), relNameGetOneVault);

            EntityTag etag = new EntityTag(Integer.toString(wrapperDTO.hashCode()));
            Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
            if (builder != null) {
                return Response.notModified().build();
            }

            return response.tag(etag).cacheControl(notStore()).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response updateVaultCredential(Integer vaultId, Integer credentialId, CredentialUpdateDTO credentialUpdateDTO) {
        try {
            long userId = vaultService.getUserIdByVaultId(vaultId);
            assertUserHasAccessToResource(identity, userId);
            Credential credential = null;
            try {
                credential = credentialService.getCredential(credentialId);
            } catch (NotFoundException e) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            EntityTag etag = new EntityTag(Integer.toString(credential.hashCode()));
            Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
            if (builder != null) {
                return builder.build();
            }
            credentialService.updateCredential(credentialId, CredentialDTOConverter.fromDTO(credentialUpdateDTO));
            return Response.noContent().link(getOneCredential(uriInfo, vaultId, credentialId), relNameGetOneCredential).cacheControl(notStore()).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}