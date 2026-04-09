package org.buglaban.travelapi.dto.response.order;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long tourId;
    private String tourName;
    private String tourCode;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private int adultQuantity;
    private int childQuantity;
    private int infantQuantity;

    private BigDecimal adultPrice;
    private BigDecimal childPrice;
    private BigDecimal infantPrice;
    private BigDecimal subtotal;
}
