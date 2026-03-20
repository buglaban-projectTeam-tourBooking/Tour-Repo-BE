package org.buglaban.travelapi.dto.request.tour;
import lombok.*;
import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourFilterRequestDTO {
    private String departure;
    private String destination;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer adultCount;
    private Integer childCount;
    private Integer infantCount;
    private String categorySlug;
    private TourStatus status;
    private String sort;         // price_asc, price_desc, rating_desc, newest
    private int page = 0;
    private int pageSize = 10;
}
