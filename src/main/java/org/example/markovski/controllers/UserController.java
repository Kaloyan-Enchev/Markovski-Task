package org.example.markovski.controllers;

import jakarta.validation.Valid;
import org.example.markovski.exceptions.AuthenticationException;
import org.example.markovski.exceptions.AuthorizationException;
import org.example.markovski.exceptions.EntityDuplicateException;
import org.example.markovski.exceptions.EntityNotFoundException;
import org.example.markovski.helpers.AuthenticationHelper;
import org.example.markovski.helpers.filters.FilterOptionsUsers;
import org.example.markovski.helpers.mappers.UserMapper;
import org.example.markovski.models.User;
import org.example.markovski.models.dtos.UserDto;
import org.example.markovski.models.dtos.UserDtoUpdating;
import org.example.markovski.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("markovski/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<User> get(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                          @RequestParam(required = false) String firstName,
                          @RequestParam(required = false) String lastName,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) String sortBy,
                          @RequestParam(required = false) String sortOrder) {
        FilterOptionsUsers filterOptionsUsers = new FilterOptionsUsers(firstName, lastName,
                email, sortBy, sortOrder);
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getAll(filterOptionsUsers);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id,
                    @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/email")
    public User getByEmail(@RequestParam String email,
                           @RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString) {
        try {
            authenticationHelper.tryGetUser(encodedString);
            return userService.getByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User userToCreate = userMapper.fromDto(userDto);
            userService.create(userToCreate);
            return userToCreate;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                       @PathVariable int id,
                       @Valid @RequestBody UserDtoUpdating userDto) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            User userToUpdate = userMapper.fromDtoUpdating(id, userDto);
            userService.update(userToUpdate, user);
            return userToUpdate;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String encodedString,
                       @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(encodedString);
            userService.deleteUser(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
