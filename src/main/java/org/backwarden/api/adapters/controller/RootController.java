package org.backwarden.api.adapters.controller;

import jakarta.ws.rs.core.Response;
import org.openapitools.api.RootApi;

public class RootController implements RootApi {
    @Override
    public Response apiGet() {
        return Response.ok().link("/users", "registerUser").build();
    }
}
