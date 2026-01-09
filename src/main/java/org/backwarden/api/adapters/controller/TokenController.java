package org.backwarden.api.adapters.controller;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.backwarden.api.adapters.controller.model.LoginRequest;
import org.backwarden.api.logic.JwtKeyGenerator;
import org.backwarden.api.logic.exceptions.DomainValidationException;
import org.backwarden.api.logic.exceptions.UserNotFoundException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Set;

import static org.backwarden.api.adapters.controller.CacheControlHelper.*;
import static org.backwarden.api.adapters.controller.LinkHelper.*;

@Path("/token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class TokenController {

    @Inject
    UserUseCase userService;

    @Context
    UriInfo uriInfo;

    @POST
    public Response generateToken(LoginRequest request) {

        if (request == null || request.email == null || request.password == null) {
            throw new BadRequestException("email + password required");
        }
        User user;
        String token;
        try {
            user = userService.authenticate(request.email, request.password);
            token = userService.generateJWTandKDF(user, request.password);
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
