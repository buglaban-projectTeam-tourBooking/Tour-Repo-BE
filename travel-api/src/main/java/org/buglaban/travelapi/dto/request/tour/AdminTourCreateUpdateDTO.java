package org.buglaban.travelapi.dto.request.tour;

import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTourCreateUpdateDTO {
    private String tourName;
    private String tourCode;
    private String slug;
    private Long categoryId;
    private String shortDescription;
    private String description;
    private String featuredImage;
    private String videoUrl;
    private String departureLocation;
    private String destination;
    private Integer durationDays;
    private Integer durationNights;
    private BigDecimal basePrice;
    private BigDecimal adultPrice;
    private BigDecimal childPrice;
    private BigDecimal infantPrice;
    private TourStatus status;
    private List<TourDayRequestDTO> itinerary;
    private List<TourScheduleRequestDTO> schedules;
}

