package org.buglaban.travelapi.dto.response.order;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long id;
    private String orderCode;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String customerNote;
    private String adminNote;
    private Date createdAt;
    private LocalDateTime paymentDate;

    // Thông tin user
    private Long userId;
    private String fullName;
    private String email;
    private String phone;

    // Danh sách tour trong đơn hàng
    private List<OrderItemResponse> items;
}
