package org.backwarden.api.adapters.controller.helper;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.ForbiddenException;

public class AuthenticationHelper {

    public static void assertUserHasAccessToResource(SecurityIdentity identity, long dbUserId) {
        long currentUserId = Long.parseLong(identity.getPrincipal().getName());
        if (currentUserId != dbUserId) {
            throw new ForbiddenException("Not your account");
        }
    }

}
