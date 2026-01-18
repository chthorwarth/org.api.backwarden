package org.backwarden.api.adapters.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.backwarden.api.logic.exceptions.UserNotFoundException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.openapitools.model.LoginRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.backwarden.api.adapters.controller.helper.CacheControlHelper.*;
import static org.backwarden.api.adapters.controller.helper.LinkHelper.*;

@Path("/token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class TokenController {

    @Inject
    UserUseCase userUseCase;

    @Context
    UriInfo uriInfo;

    @POST
    public Response generateToken(LoginRequest request) {

        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new BadRequestException("email + password required");
        }
        User user;
        String token;
        try {
            user = userUseCase.authenticate(request.getEmail(), request.getPassword());
            token = userUseCase.generateJWTandKDF(user, request.getPassword());
        } catch (UserNotFoundException e) {
            throw new NotAuthorizedException("Invalid credentials");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException();
        }
        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        return Response.created(null).link(getAllVaults(uriInfo, user.getId()), relNameGetAllVaults).link(getOneUser(uriInfo, user.getId()), relNameGetOneUser).entity(token).cacheControl(notStore()).build();
    }
}
