package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import org.backwarden.api.adapters.controller.model.converter.UserDTOConverter;
import org.backwarden.api.logic.exceptions.DomainValidationException;
import org.backwarden.api.logic.exceptions.EmailAlreadyExistsException;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserRegistrationDTO;

@ApplicationScoped
public class UserController implements UsersApi {
    @Inject
    UserUseCase userService;

    @Override
    public Response usersPost(@Valid @NotNull UserRegistrationDTO userRegistrationDTO) {
        try {
            userService.createUser(UserDTOConverter.fromDTO(userRegistrationDTO));
            return Response.status(Response.Status.CREATED).build();
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
        UserDTO dto = UserDTOConverter.toDTO(userService.getUser(userId));

        return Response.ok(dto).build();
    }
}
