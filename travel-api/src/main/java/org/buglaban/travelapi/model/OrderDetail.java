package org.buglaban.travelapi.model;
// ORDER DETAIL ENTITY - Chi tiết đơn đặt tour
import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.Gender;
import org.buglaban.travelapi.util.ParticipantType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Quan hệ Many-to-One với Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Quan hệ Many-to-One với Tour
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    // Quan hệ Many-to-One với TourSchedule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_schedule_id", nullable = false)
    private TourSchedule tourSchedule;

    @Column(name = "tour_name", nullable = false, length = 500)
    private String tourName;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    // Số lượng người
    @Column(name = "adult_quantity")
    private Integer adultQuantity = 0;

    @Column(name = "child_quantity")
    private Integer childQuantity = 0;

    @Column(name = "infant_quantity")
    private Integer infantQuantity = 0;

    // Giá
    @Column(name = "adult_price", precision = 15, scale = 2)
    private BigDecimal adultPrice = BigDecimal.ZERO;

    @Column(name = "child_price", precision = 15, scale = 2)
    private BigDecimal childPrice = BigDecimal.ZERO;

    @Column(name = "infant_price", precision = 15, scale = 2)
    private BigDecimal infantPrice = BigDecimal.ZERO;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    // Quan hệ One-to-Many với OrderParticipant
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OrderParticipant> participants = new HashSet<>();

    // Helper methods
    public void calculateSubtotal() {
        BigDecimal adultTotal = adultPrice.multiply(BigDecimal.valueOf(adultQuantity));
        BigDecimal childTotal = childPrice.multiply(BigDecimal.valueOf(childQuantity));
        BigDecimal infantTotal = infantPrice.multiply(BigDecimal.valueOf(infantQuantity));
        this.subtotal = adultTotal.add(childTotal).add(infantTotal);
    }

    public Integer getTotalParticipants() {
        return adultQuantity + childQuantity + infantQuantity;
    }
}




