package org.example.markovski.helpers.mappers;

import org.example.markovski.models.User;
import org.example.markovski.models.dtos.RegisterDto;
import org.example.markovski.models.dtos.UserDto;
import org.example.markovski.models.dtos.UserDtoUpdating;
import org.example.markovski.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(int id, UserDto userDto) {
        User oldUser = userService.getById(id);
        User user = fromDto(userDto);
        user.setId(id);
        user.setDateOfBirth(oldUser.getDateOfBirth());
        return user;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public User fromDtoUpdating(int id, UserDtoUpdating userDto) {
        User user = fromDtoUpdating(userDto);
        user.setId(id);
        return user;
    }
    public User fromDtoUpdating(UserDtoUpdating userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public User fromDtoRegister(RegisterDto registerDto) {
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setDateOfBirth(registerDto.getDateOfBirth());
        return user;
    }
}
