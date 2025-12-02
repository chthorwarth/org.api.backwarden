package org.backwarden.api.adapters.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.backwarden.api.adapters.database.CredentialRepository;
import org.backwarden.api.adapters.database.UserRepository;
import org.backwarden.api.adapters.database.model.CredentialEntity;
import org.backwarden.api.adapters.database.model.UserEntity;

@Path("/user")
@ApplicationScoped
public class UserController {
    @Inject
    UserRepository userRepository;

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
        userRepository.saveUser(user);

        UserEntity userNew = userRepository.getUser(1);
        System.out.println(userNew.getMasterEmail());
        return userNew.getMasterEmail();

    }
}
