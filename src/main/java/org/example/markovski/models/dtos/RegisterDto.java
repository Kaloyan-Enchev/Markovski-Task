package org.example.markovski.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class RegisterDto extends LoginDto{
    @NotEmpty(message = "Password confirmation can't be empty!")
    private String passwordConfirm;
    @NotEmpty(message = "First name can't be empty!")
    @Size(min = 2, max = 20, message = "First name must be between 2 symbols and 20 symbols.")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty!")
    @Size(min = 2, max = 20, message = "Last name must be between 2 symbols and 20 symbols.")
    private String lastName;
    @NotEmpty(message = "Phone number can't be empty!")
    private String phoneNumber;
    private String dateOfBirth;
}
