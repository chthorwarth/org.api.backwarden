package org.backwarden.api.adapters.controller;


import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.QueryParam;
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
import org.openapitools.model.CredentialWrapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import static org.backwarden.api.adapters.controller.LinkHelper.*;

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

            return Response.noContent().link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).build();
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

            return Response.ok(credentialDTO).link(deleteCredential(uriInfo, vaultId, credentialId), relNameDeleteCredential).link(getAllCredentials(uriInfo, vaultId), relNameGetAllCredentials).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (SecurityException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response vaultsVaultIdCredentialsGet(Integer vaultId, @QueryParam("title") String title) {
        try {
            long currentUserId = Long.parseLong(identity.getPrincipal().getName());
            long userId = vaultService.getUserIdByVaultId(vaultId);

            if (currentUserId != userId) {
                throw new ForbiddenException("Not your account");
            }

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
                dto.setSelfLink(credentialLocation(uriInfo, vaultId, dto.getId()));

            wrapperDTO.credentialDTOS(credentialDTOs);
            wrapperDTO.setSelfLink(createPaginationUri(page, size, vaultId));

            URI selfUri = createPaginationUri(page, size, vaultId);
            URI nextUri = createPaginationUri(page + 1, size, vaultId);
            URI prevUri = page > 0 ? createPaginationUri(page - 1, size, vaultId) : null;

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

            return response.build();

        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private URI createPaginationUri(int page, int size, Integer vaultId) {
        return uriInfo.getBaseUriBuilder()
                .path("/vaults/{vaultid}/credentials")
                .resolveTemplate("vaultid", vaultId)
                .queryParam("page", page)
                .queryParam("size", size)
                .build();
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
            return Response.created(credentialLocation(uriInfo, vaultId, credentialid)).build();
        } catch (NoSuchElementException e) {
            log.info(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (CryptionGoneWrongException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}