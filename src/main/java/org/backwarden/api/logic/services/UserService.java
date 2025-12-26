package org.backwarden.api.logic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.backwarden.api.adapters.database.model.UserEntity;
import org.backwarden.api.logic.exceptions.DomainValidationException;
import org.backwarden.api.logic.model.User;
import org.backwarden.api.logic.ports.input.UserUseCase;
import org.backwarden.api.logic.ports.output.persistence.UserRepository;

import java.util.regex.Pattern;
import io.quarkus.elytron.security.common.BcryptUtil;

@ApplicationScoped
public class UserService implements UserUseCase
{
    private static final String EMAIL_REGEX = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){12,30}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Inject
    UserRepository userRepository;


    @Override
    public void createUser(User user)
    {
        if (isMailValid(user.getMasterEmail()) && isPasswordValid(user.getMasterPassword(), user.getMasterEmail())) {
            String passwordHash = BcryptUtil.bcryptHash(user.getMasterPassword());

            // 3. User-Objekt erstellen und in DB speichern

            user.setMasterPasswordHash(passwordHash); // Speichere NUR den Hash

            userRepository.saveUser(user);
        }
        else{
            throw new DomainValidationException("Invalid email or password");
        }

    }

    @Override
    public User getUser(long id)
    {
        return userRepository.getUser(id);
    }


    boolean isMailValid(String mail)
    {
        if (mail == null || mail.isEmpty()) {
            return false;
        }
        return PATTERN.matcher(mail).matches();
    }
    boolean isPasswordValid(String password, String mail)
    {
        if (password == null || password.isEmpty() || password.equals(mail)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}


