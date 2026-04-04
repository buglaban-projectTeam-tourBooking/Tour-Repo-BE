package org.buglaban.travelapi.dto.response.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String tourName;
    private LocalDateTime createdAt;
    private String orderStatus;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private String paymentMethod;
}
