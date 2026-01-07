package org.backwarden.api.adapters.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.backwarden.api.adapters.controller.model.converter.UserDTOConverter;
import org.backwarden.api.logic.exceptions.DomainValidationException;
import org.backwarden.api.logic.exceptions.EmailAlreadyExistsException;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserRegistrationDTO;

import java.net.URI;

@ApplicationScoped
public class UserController implements UsersApi {
    @Inject
    UserUseCase userService;

    @Context
    UriInfo uriInfo;

    @Inject
    SecurityIdentity identity;

    @Override
    public Response usersPost(@Valid @NotNull UserRegistrationDTO userRegistrationDTO) {
        try {
            long id = userService.createUser(UserDTOConverter.fromDTO(userRegistrationDTO));
            URI location = uriInfo
                    .getAbsolutePathBuilder()
                    .path(String.valueOf(id))
                    .build();
            URI token = uriInfo
                    .getBaseUriBuilder()
                    .path("token")
                    .build();
            return Response.created(location).link(token, "generateToken").build();
        } catch (DomainValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (EmailAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Override
    public Response usersUserIdGet(Integer userId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());

        if (currentUserId != userId) {
            throw new ForbiddenException("Not your account");
        }

        UserDTO dto = UserDTOConverter.toDTO(userService.getUser(userId));

        return Response.ok(dto).build();
    }
}
