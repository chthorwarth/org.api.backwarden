package org.backwarden.api.adapters.controller;

import jakarta.ws.rs.core.*;
import org.openapitools.api.RootApi;

import static org.backwarden.api.adapters.controller.helper.LinkHelper.*;
import static org.backwarden.api.adapters.controller.helper.CacheControlHelper.*;

public class RootController implements RootApi {

    @Context
    UriInfo uriInfo;

    @Context
    Request req;

    @Override
    public Response getApiRoot() {
        String rep = "registerUser=" + registerUser(uriInfo) + "generateToke=" + createToken(uriInfo);
        String hash = Integer.toHexString(rep.hashCode());
        EntityTag etag = new EntityTag(hash);
        Response.ResponseBuilder builder = req.evaluatePreconditions(etag);
        if (builder != null) {
            return Response.notModified().build();
        }
        return Response.ok().link(registerUser(uriInfo), relNameRegisterUser).link(createToken(uriInfo), relNameGenerateToken).cacheControl(cachePublicMustRevalidate()).tag(etag).build();
    }
}
