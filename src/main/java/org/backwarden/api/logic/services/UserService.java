package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;

@ApplicationScoped
public class UserService implements UserUseCase
{
    @Inject
    UserRepository userRepository;


    @Override
    public void createUser(User user)
    {
        userRepository.saveUser(user);
    }

    @Override
    public User getUser(long id)
    {
        return userRepository.getUser(id);
    }
}
