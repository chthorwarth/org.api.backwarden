package org.backwarden.api.adapters.controller;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.openapitools.api.RootApi;

import static org.backwarden.api.adapters.controller.LinkHelper.*;

public class RootController implements RootApi {

    @Context
    UriInfo uriInfo;

    @Override
    public Response apiGet() {
        return Response.ok().link(registerUser(uriInfo), relNameRegisterUser).build();
    }
}
