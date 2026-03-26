package org.buglaban.travelapi.dto.request.tour;
import lombok.*;
import org.buglaban.travelapi.util.ScheduleStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourScheduleRequestDTO {
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer availableSeats;
    private String note;
    private ScheduleStatus status;
}
