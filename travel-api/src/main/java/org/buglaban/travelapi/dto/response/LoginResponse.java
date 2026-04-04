package org.buglaban.travelapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.buglaban.travelapi.model.Role;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("token")
    private String token;
    @JsonProperty("name")
    private String name;
    @JsonProperty("role")
    private String role;

    public LoginResponse(String token, String role, String name) {
        this.token = token;
        this.name = name;
        this.role = role;
    }
}
