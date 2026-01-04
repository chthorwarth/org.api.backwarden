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
import org.backwarden.api.logic.exceptions.UserDoesNotExistException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;

import java.net.URI;
import java.time.Duration;
import java.util.Set;

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
        User user = null;
        try {
            user = userService.authenticate(request.email, request.password);
        } catch (UserDoesNotExistException e) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        URI vaults = uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults")
                .resolveTemplate("userid", user.getId())
                .build();

        String token = Jwt.issuer("http://org.backwarden.api")
                //.upn(user.getMasterEmail())
                .subject(String.valueOf(user.getId()))
                .groups(Set.of("user"))
                .expiresIn(Duration.ofHours(2))
                .sign(JwtKeyGenerator.PRIVATE_KEY);
        return Response.created(null).link(vaults, "getAllVaults").entity(token).build();
    }
}
