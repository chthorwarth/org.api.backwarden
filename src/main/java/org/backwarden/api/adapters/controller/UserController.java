package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import org.backwarden.api.adapters.controller.model.converter.UserDTOConverter;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserDTO;

@ApplicationScoped
public class UserController implements UsersApi
{
    @Inject
    UserUseCase userService;

    @Override
    public Response usersPost(@Valid @NotNull UserDTO userDTO)
    {
        userService.createUser(UserDTOConverter.fromDTO(userDTO));
        return Response.ok().build();
    }

    @Override
    public Response usersUserIdGet(Integer userId)
    {
        UserDTO dto = UserDTOConverter.toDTO(userService.getUser(userId));

        return Response.ok(dto).build();
    }
}
