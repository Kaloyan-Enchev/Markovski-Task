package org.example.markovski.services;

import org.example.markovski.exceptions.EntityDuplicateException;
import org.example.markovski.exceptions.EntityNotFoundException;
import org.example.markovski.helpers.filters.FilterOptionsUsers;
import org.example.markovski.models.User;
import org.example.markovski.repositories.contracts.UserRepository;
import org.example.markovski.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll(FilterOptionsUsers filterOptionsUsers) {
        return repository.getAll(filterOptionsUsers);
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    @Override
    public void create(User userToCreate) {
        boolean duplicateEmailExists = true;

        try {
            repository.getByEmail(userToCreate.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateEmailExists = false;
        }


        if (duplicateEmailExists) {
            throw new EntityDuplicateException("User", "email", userToCreate.getEmail());
        }

        repository.create(userToCreate);
    }

    @Override
    public void update(User userToUpdate, User user) {
        boolean duplicateExists = true;
        try {
            User existingUser = repository.getByEmail(userToUpdate.getEmail());
            if (existingUser.getId() == userToUpdate.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", userToUpdate.getEmail());
        }

        repository.update(userToUpdate);
    }

    @Override
    public void deleteUser(int id, User user) {
        repository.deleteUser(id);
    }
}
