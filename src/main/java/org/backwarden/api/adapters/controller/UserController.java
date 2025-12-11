package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.backwarden.api.adapters.database.UserAdapter;
import org.backwarden.api.adapters.database.model.UserEntity;

@Path("/user")
@ApplicationScoped
public class UserController {
    @Inject
    UserAdapter userAdapter;

	/*private final CredentialAPI credentialService;

	public CredentialController(CredentialAPI credentialService) {
		this.credentialService = credentialService;
	}

	public void createCredentials(String username, String password) {
		credentialService.createCredentials(username, password);
	}*/

    @GET
    public String hello() {
        System.out.println("Hello World");
        UserEntity user = new UserEntity();
        user.setMasterEmail("simon@thws");
        userAdapter.saveUser(user);

//        UserEntity userNew = userAdapter.getUser(1);
//        System.out.println(userNew.getMasterEmail());


        return "hello";

    }
}
