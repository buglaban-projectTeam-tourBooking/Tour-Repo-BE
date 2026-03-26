package org.buglaban.travelapi.dto.response.tour;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buglaban.travelapi.util.ScheduleStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourScheduleDTO {
    private Long id;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer availableSeats;
    private Integer bookedSeats;
    private Integer availableSlots;   // availableSeats - bookedSeats
    private ScheduleStatus status;
    private String note;
}
