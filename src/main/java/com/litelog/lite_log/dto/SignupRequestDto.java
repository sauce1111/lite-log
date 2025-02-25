package com.litelog.lite_log.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "username is required.")
    private String username;

    @NotBlank(message = "password is required.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one letter, one number, and one special character."
    )
    private String password;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    private String nickname;
}
