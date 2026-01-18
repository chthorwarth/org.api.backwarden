package org.backwarden.api.adapters.controller;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class LinkHelper {

    public final static String relNameGetAllVaults = "getAllVaults";
    public final static String relNameGetOneVault = "getOneVault";
    public final static String relNameGetAllCredentials = "getAllCredentials";
    public final static String relNameGetOneCredential = "getOneCredential";
    public final static String relNameDeleteVault = "deleteVault";
    public final static String relNameCreateVault = "createVault";
    public final static String relNameUpdateVault = "updateVault";
    public final static String relNameUpdateCredential = "updateCredential";
    public final static String relNameRegisterUser = "registerUser";
    public final static String relNameCreateCredentials = "createCredential";
    public final static String relNameDeleteCredential = "deleteCredential";
    public final static String relNameGenerateToken = "generateToken";
    public final static String relNameGetOneUser = "getOneUser";

    public static URI getOneUser(UriInfo uriInfo, long userId) {
        return uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}")
                .resolveTemplate("userid", userId)
                .build();
    }

    public static URI createToken(UriInfo uriInfo) {
        return uriInfo
                .getBaseUriBuilder()
                .path("token")
                .build();
    }


    public static URI getAllVaults(UriInfo uriInfo, long userId) {
        return uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults")
                .resolveTemplate("userid", userId)
                .build();
    }

    public static URI getOneVault(UriInfo uriInfo, long userId, long vaultid) {
        return uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults/{vaultid}")
                .resolveTemplate("userid", userId)
                .resolveTemplate("vaultid", vaultid)
                .build();
    }

    public static URI getAllCredentials(UriInfo uriInfo, long vaultid) {
        return uriInfo
                .getBaseUriBuilder()
                .path("vaults/{vaultid}/credentials")
                .resolveTemplate("vaultid", vaultid)
                .build();
    }

    public static URI deleteVault(UriInfo uriInfo, long userId, long vaultid) {
        return uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults/{vaultid}")
                .resolveTemplate("userid", userId)
                .resolveTemplate("vaultid", vaultid)
                .build();
    }

    public static URI createVault(UriInfo uriInfo, long userid) {
        return uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults")
                .resolveTemplate("userid", userid)
                .build();
    }

    public static URI updateVault(UriInfo uriInfo, long userid, long vaultid) {
        return uriInfo
                .getBaseUriBuilder()
                .path("users/{userid}/vaults/{vaultid}")
                .resolveTemplate("userid", userid)
                .resolveTemplate("vaultid", vaultid)
                .build();
    }

    public static URI updateCredential(UriInfo uriInfo, long vaultid, long credentialid) {
        return uriInfo.getBaseUriBuilder()
                .path("/vaults/{vaultid}/credentials/{credentialid}")
                .resolveTemplate("credentialid", credentialid)
                .resolveTemplate("vaultid", vaultid)
                .build();
    }

    public static URI registerUser(UriInfo uriInfo) {
        return uriInfo
                .getBaseUriBuilder()
                .path("/users")
                .build();
    }

    public static URI createCredentials(UriInfo uriInfo, long vaultId) {
        return uriInfo
                .getBaseUriBuilder()
                .path("vaults/{vaultid}/credentials")
                .resolveTemplate("vaultid", vaultId)
                .build();
    }

    public static URI getOneCredential(UriInfo uriInfo, long vaultid, long credentialid) {
        return uriInfo.getBaseUriBuilder()
                .path("/vaults/{vaultid}/credentials/{credentialid}")
                .resolveTemplate("credentialid", credentialid)
                .resolveTemplate("vaultid", vaultid)
                .build();
    }

    public static URI deleteCredential(UriInfo uriInfo, long vaultid, long credentialid) {
        return uriInfo
                .getBaseUriBuilder()
                .path("vaults/{vaultid}/credentials/{credentialid}")
                .resolveTemplate("vaultid", vaultid)
                .resolveTemplate("credentialid", credentialid)
                .build();
    }


}
