package org.backwarden.api.adapters.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.*;
import org.backwarden.api.adapters.controller.model.converter.UserDTOConverter;
import org.backwarden.api.logic.exceptions.DomainValidationException;
import org.backwarden.api.logic.exceptions.EmailAlreadyExistsException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserRegistrationDTO;

import java.net.URI;

import static org.backwarden.api.adapters.controller.helper.CacheControlHelper.*;
import static org.backwarden.api.adapters.controller.helper.AuthenticationHelper.*;

import static org.backwarden.api.adapters.controller.helper.LinkHelper.*;

@ApplicationScoped
public class UserController implements UsersApi {
    @Inject
    UserUseCase userService;

    @Context
    UriInfo uriInfo;

    @Inject
    SecurityIdentity identity;

    @Context
    Request req;

    @Override
    public Response createUser(UserRegistrationDTO userRegistrationDTO) {
        try {
            long id = userService.createUser(UserDTOConverter.fromDTO(userRegistrationDTO));
            URI location = uriInfo
                    .getAbsolutePathBuilder()
                    .path(String.valueOf(id))
                    .build();
            return Response.created(location).link(createToken(uriInfo), relNameGenerateToken).cacheControl(notStore()).build();
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
    public Response getUserById(Integer userId) {
        assertUserHasAccessToResource(identity, userId);

        User user = userService.getUser(userId);
        UserDTO dto = UserDTOConverter.toDTO(user);
        EntityTag etag = new EntityTag(Integer.toString(user.hashCode()));
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return builder.build();
        }
        return Response.ok(dto).cacheControl(cachePrivateMustRevalidate()).tag(etag).build();
    }
}
