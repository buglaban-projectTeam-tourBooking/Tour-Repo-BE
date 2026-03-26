package org.buglaban.travelapi.dto.response.tour;

import lombok.*;
import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourSummaryDTO {
    private Long id;
    private String tourName;
    private String slug;
    private String tourCode;
    private String featuredImage;
    private String destination;
    private String departureLocation;
    private Integer durationDays;
    private Integer durationNights;
    private BigDecimal adultPrice;
    private BigDecimal priceFrom;       // giá thấp nhất từ schedule
    private TourStatus status;
    private Double averageRating;
    private Long totalReviews;
    private String categoryName;
}