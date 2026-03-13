package org.buglaban.travelapi.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO implements Serializable {
    @NotBlank (message = "full name must be not blank")
    private String fullName;
    @Email (message = "email invalid format")
    private String email;
    @NotNull (message = "password must be not null")
    private String passwordHash;
}
