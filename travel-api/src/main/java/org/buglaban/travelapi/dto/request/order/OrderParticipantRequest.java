package org.buglaban.travelapi.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OrderParticipantRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dateOfBirth;

    private String gender;                    // MALE, FEMALE, OTHER

    private String passportNumber;

    private String nationality;

    private String participantType;           // ADULT, CHILD, INFANT

    private String specialRequirements;
}