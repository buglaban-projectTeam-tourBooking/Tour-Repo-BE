package org.buglaban.travelapi.dto.response.tour;

import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTourSummaryDTO {
    private Long id;
    private String tourName;
    private String tourCode;
    private String slug;
    private TourStatus status;
    private BigDecimal adultPrice;
    private String destination;
    private String categoryName;
    private String createdByName;
    private Long totalBookings;
    private Long availableSchedulesCount;
}
