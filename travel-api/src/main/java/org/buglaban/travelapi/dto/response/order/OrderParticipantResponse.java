package org.buglaban.travelapi.dto.response.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OrderParticipantResponse {
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String passportNumber;
    private String participantType;
    private String specialRequirements;
}
