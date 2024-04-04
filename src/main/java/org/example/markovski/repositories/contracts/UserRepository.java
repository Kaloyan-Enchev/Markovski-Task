package org.example.markovski.repositories.contracts;

import org.example.markovski.helpers.filters.FilterOptionsUsers;
import org.example.markovski.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll(FilterOptionsUsers filterOptionsUsers);

    User getById(int id);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void deleteUser(int id);
}
