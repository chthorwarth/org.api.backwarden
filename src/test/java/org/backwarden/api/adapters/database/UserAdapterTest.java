package org.backwarden.api.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.backwarden.api.logic.model.Credential;
import org.backwarden.api.logic.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserAdapterTest
{
    @Inject
    UserAdapter adapter;

    //Test of hexagonal architecture (return value)
    @Test
    @Transactional
    void getUser()
    {
        User user = adapter.getUser(1);
        assertEquals(User.class, user.getClass());
    }
}