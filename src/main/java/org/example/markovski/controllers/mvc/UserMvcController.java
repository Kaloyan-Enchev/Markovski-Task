package org.example.markovski.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.markovski.exceptions.AuthenticationException;
import org.example.markovski.exceptions.AuthorizationException;
import org.example.markovski.exceptions.EntityDuplicateException;
import org.example.markovski.exceptions.EntityNotFoundException;
import org.example.markovski.helpers.AuthenticationHelper;
import org.example.markovski.helpers.filters.FilterOptionsUsers;
import org.example.markovski.helpers.mappers.UserMapper;
import org.example.markovski.models.User;
import org.example.markovski.models.dtos.FilterDtoUser;
import org.example.markovski.models.dtos.UserDto;
import org.example.markovski.models.dtos.UserDtoUpdating;
import org.example.markovski.services.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper,
                             UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllUsers(@ModelAttribute("filterOptions") FilterDtoUser filterDto, Model model,
                               HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        FilterOptionsUsers filterOptions = new FilterOptionsUsers(
                filterDto.getFirstName(),
                filterDto.getLastName(),
                filterDto.getEmail(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());

        List<User> users = userService.getAll(filterOptions);
        if (populateIsAuthenticated(session)){
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByEmail(currentUsername));
        }
        model.addAttribute("filterOptionsUsers", filterDto);
        model.addAttribute("users", users);
        return "UsersView";
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            try {
                authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }
            if (populateIsAuthenticated(session)){
                String currentUsername = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByEmail(currentUsername));
            }

            User user = userService.getById(id);

            model.addAttribute("user", user);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/deletes/{id}")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session){
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.deleteUser(id, user);
            session.removeAttribute("currentUser");
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/edit")
    public String showUserEditPage (Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }

        if (populateIsAuthenticated(session)){
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByEmail(currentUsername));
        }

        model.addAttribute("userEdit", new UserDtoUpdating());
        return "UserEditView";
    }

    @PostMapping("/edit")
    public String handleUserEdit(@Valid @ModelAttribute("userEdit") UserDtoUpdating userDtoUpdating,
                                 BindingResult bindingResult,
                                 HttpSession session) {
        if(bindingResult.hasErrors()) {
            return "UserEditView";
        }
        try {
            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationException e) {
                return "redirect:/auth/login";
            }

            User userToUpdate = userMapper.fromDtoUpdating(userDtoUpdating);
            userToUpdate.setId(user.getId());
            userToUpdate.setEmail(user.getEmail());
            userService.update(userToUpdate, user);
            session.removeAttribute("currentUser");
            session.setAttribute("currentUser", userToUpdate.getEmail());
            return "redirect:/users";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "UserEditView";
        }
    }
}
