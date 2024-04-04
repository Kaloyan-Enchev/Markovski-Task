package org.example.markovski.services.contracts;

import org.example.markovski.helpers.filters.FilterOptionsUsers;
import org.example.markovski.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(FilterOptionsUsers filterOptionsUsers);

    User getById(int id);

    User getByEmail(String email);

    void create(User userToCreate);

    void update(User userToUpdate, User user);

    void deleteUser(int id, User user);
}
