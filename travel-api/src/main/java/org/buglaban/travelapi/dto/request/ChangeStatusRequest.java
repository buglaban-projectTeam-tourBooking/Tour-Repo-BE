package org.buglaban.travelapi.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.buglaban.travelapi.util.UserStatus;
@Setter
@Getter
public class ChangeStatusRequest {
    private UserStatus status;
}
