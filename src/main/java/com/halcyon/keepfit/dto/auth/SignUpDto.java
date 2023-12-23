package com.halcyon.keepfit.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    @Email
    private String email;

    @Size(min = 2, max = 50, message = "Firstname must be more than 1 character and less than 50 characters.")
    private String firstname;

    @Size(min = 2, max = 50, message = "Lastname must be more than 1 character and less than 50 characters.")
    private String lastname;

    @Size(min = 5, message = "Password must be more than 5 characters.")
    private String password;

    @Size(max = 64, message = "About must be less than 64 characters.")
    private String about;
}
