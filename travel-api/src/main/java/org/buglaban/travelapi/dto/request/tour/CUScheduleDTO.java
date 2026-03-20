package org.buglaban.travelapi.dto.request.tour;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CUScheduleDTO {

    @NotNull(message = "Departure date không được để trống")
    @JsonFormat (pattern = "yyyy-MM-dd")
    @Future (message = "Ngày khởi hành phải nằm trong tương lai")
    private LocalDate departureDate;

    @NotNull(message = "Return date không được để trống")
    @JsonFormat (pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    @NotNull(message = "Title không được để trống")
    private String titleTourSchedule;

    @NotNull(message = "slots không được để trống")
    @Min(value = 1, message = "Available slots phải lớn hơn 1")
    @Max(value = 1000, message = "Available slots không quá 1000")
    private Integer availableSlots;

    @AssertTrue (message = "Return date phair sau departure date")
    public boolean isDateRangeValid () {
        if (departureDate == null || returnDate == null) {
            return true;
        }
        return returnDate.isAfter(departureDate);
    }

}
