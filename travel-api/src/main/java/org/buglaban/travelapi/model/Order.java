package org.buglaban.travelapi.model;

// ORDER ENTITY - Đơn đặt tour

import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.OrderStatus;
import org.buglaban.travelapi.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends AbstractEntity {

    @Column(name = "order_code", unique = true, nullable = false, length = 50)
    private String orderCode;

    // Quan hệ Many-to-One với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Thông tin đơn hàng
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "final_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal finalAmount;

    // Trạng thái
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    // Thanh toán
    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // cash, bank_transfer, credit_card, momo, vnpay

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "transaction_id")
    private String transactionId;

    // Ghi chú
    @Column(name = "customer_note", columnDefinition = "TEXT")
    private String customerNote;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    // Quan hệ One-to-Many với OrderDetail
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    // Helper methods
    public void calculateFinalAmount() {
        this.finalAmount = this.totalAmount.subtract(this.discountAmount);
    }
}
