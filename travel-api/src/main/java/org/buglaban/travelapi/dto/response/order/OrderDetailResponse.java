package org.buglaban.travelapi.dto.response.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long id;
    private String orderCode;
    private String tourName;
    private Long tourId;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String customerNote;
    private String adminNote;

    private List<OrderParticipantResponse> participants;
}
