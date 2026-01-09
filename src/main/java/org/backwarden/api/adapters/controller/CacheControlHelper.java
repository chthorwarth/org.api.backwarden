package org.backwarden.api.adapters.controller;

import jakarta.ws.rs.core.CacheControl;

public class CacheControlHelper {

    public static CacheControl cachePublicMustRevalidate() {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setMustRevalidate(true);
        return cacheControl;
    }

    public static CacheControl cachePrivateMustRevalidate() {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setNoCache(true);
        cacheControl.setMustRevalidate(true);
        return cacheControl;
    }

    public static CacheControl notStore() {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoStore(true);
        return cacheControl;
    }
}
