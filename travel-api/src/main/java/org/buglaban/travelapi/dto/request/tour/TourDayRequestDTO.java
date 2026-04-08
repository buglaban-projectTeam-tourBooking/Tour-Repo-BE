package org.buglaban.travelapi.dto.request.tour;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDayRequestDTO {

    @NotBlank(message = "Số ngày không được để trống")
    private String dayNumber;

    @NotBlank(message = "Tiêu đề ngày không được để trống")
    private String dayTitle;

    private String dep;

    @NotBlank(message = "Mô tả ngày không được để trống")
    private String description;

    private String image;
}
