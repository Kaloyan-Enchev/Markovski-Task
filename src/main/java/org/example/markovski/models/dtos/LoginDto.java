package org.example.markovski.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotEmpty(message = "Email can't be empty!")
    private String email;
    @NotEmpty(message = "Password can't be empty.")
    private String password;

    public LoginDto() {
    }
}
