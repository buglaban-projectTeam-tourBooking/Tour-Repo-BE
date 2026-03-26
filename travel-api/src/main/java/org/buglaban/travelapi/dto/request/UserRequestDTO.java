package org.buglaban.travelapi.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.buglaban.travelapi.model.Role;
import org.buglaban.travelapi.util.Gender;
import org.buglaban.travelapi.util.UserStatus;
import org.buglaban.travelapi.util.UserType;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class UserRequestDTO {
    @NotBlank(message = "full name must be not blank")
    private String fullName;
    @NotBlank(message = "email must not be blank")
    @Email(message = "email invalid format")
    private String email;
    @NotNull(message = "password must be not null")
    private String passwordHash;
    @NotBlank(message = "phone must be not blank")
    private String phone;
    @NotBlank(message = "avatarUrl must be not blank")
    private String avatarUrl;
    @NotNull(message = "dateOfBirth must be not blank")
    private LocalDate dateOfBirth;
    @NotNull(message = "gender must be not blank")
    private Gender gender;
    @NotBlank(message = "address must be not blank")
    private String address;
    @NotNull(message = "status must be not blank")
    private UserStatus status = UserStatus.ACTIVE;
    @NotNull(message = "role must be not blank")
    private UserType userType;
}
