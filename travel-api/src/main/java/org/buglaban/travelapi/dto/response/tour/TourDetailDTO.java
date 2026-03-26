package org.buglaban.travelapi.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buglaban.travelapi.dto.response.CategoryDTO;
import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetailDTO {
    private Long id;
    private String tourName;
    private String slug;
    private String tourCode;
    private String shortDescription;
    private String description;
    private String featuredImage;
    private String videoUrl;
    private String destination;
    private String departureLocation;
    private Integer durationDays;
    private Integer durationNights;
    private BigDecimal adultPrice;
    private BigDecimal childPrice;
    private BigDecimal infantPrice;
    private TourStatus status;
    private Integer viewCount;
    private Double averageRating;
    private Long totalReviews;
    private String categoryName;
    private String categorySlug;
    private List<TourDayDTO> itinerary;
    private List<TourScheduleDTO> schedules;
}
