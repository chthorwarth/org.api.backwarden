package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.openapitools.api.UsersApi;
import org.openapitools.model.UserDTO;

import java.util.List;

@ApplicationScoped
public class UserController implements UsersApi {

    @Override
    public Response usersPost(@Valid @NotNull UserDTO userDTO) {
        System.out.println(userDTO.getMasterEmail());
        return Response.ok().build();
    }


    @Override
    public Response usersUserIdGet(Integer userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.id(1L);
        userDTO.setMasterEmail("test@test.de");
        return Response.ok().build();
    }

//    @Inject
//    UserAdapter userAdapter;

	/*private final CredentialAPI credentialService;

	public CredentialController(CredentialAPI credentialService) {
		this.credentialService = credentialService;
	}

	public void createCredentials(String username, String password) {
		credentialService.createCredentials(username, password);
	}*/
//
//    @GET
//    public String hello() {
//        System.out.println("Hello World");
//        UserEntity user = new UserEntity();
//        user.setMasterEmail("simon@thws");
//        userAdapter.saveUser(user);
//

    /// /        UserEntity userNew = userAdapter.getUser(1);
    /// /        System.out.println(userNew.getMasterEmail());
//
//
//        return "hello";
//
//    }

}
