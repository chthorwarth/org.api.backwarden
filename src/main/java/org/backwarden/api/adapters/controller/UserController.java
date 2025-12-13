package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import org.backwarden.api.adapters.controller.model.UserDTO;
import org.backwarden.api.adapters.controller.model.interfaces.UsersApi;
import org.backwarden.api.adapters.database.UserAdapter;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.model.User;


import java.util.List;

@ApplicationScoped
public class UserController implements UsersApi {

    @Override
    public void usersPost(@Valid @NotNull UserDTO userDTO) {
        System.out.println(userDTO.getMasterEmail());
    }


    @Override
    public UserDTO usersUserIdGet(Integer userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setMasterEmail("test@test.de");
        return userDTO;
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
