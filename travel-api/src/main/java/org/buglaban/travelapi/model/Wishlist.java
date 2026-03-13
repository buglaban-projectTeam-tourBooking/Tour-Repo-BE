package org.buglaban.travelapi.model;

// WISHLIST ENTITY - Danh sách yêu thích

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlists", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "tour_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}


// CART ENTITY - Giỏ hàng

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "tour_schedule_id", nullable = false)
    private Integer tourScheduleId;

    @Column(name = "adult_quantity")
    private Integer adultQuantity = 1;

    @Column(name = "child_quantity")
    private Integer childQuantity = 0;

    @Column(name = "infant_quantity")
    private Integer infantQuantity = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}


// NOTIFICATION ENTITY - Thông báo

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "notification_type", length = 50)
    private String notificationType; // order, review, promotion, system

    @Column(name = "reference_id")
    private Integer referenceId; // ID của order, review, etc.

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

// CONTACT ENTITY - Liên hệ

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "admin_reply", columnDefinition = "TEXT")
    private String adminReply;

    @Column(name = "replied_at")
    private LocalDateTime repliedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}

// SETTINGS ENTITY - Cấu hình hệ thống

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "setting_key", unique = true, nullable = false, length = 100)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "setting_type", length = 50)
    private String settingType; // text, number, boolean, json

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "group_name", length = 100)
    private String groupName; // general, email, payment, seo

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

// STATISTICS DAILY ENTITY - Thống kê theo ngày

@Entity
@Table(name = "statistics_daily", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"stat_date"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class StatisticsDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "stat_date", nullable = false, unique = true)
    private java.time.LocalDate statDate;

    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    @Column(name = "total_revenue", precision = 15, scale = 2)
    private java.math.BigDecimal totalRevenue = java.math.BigDecimal.ZERO;

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;

    @Column(name = "new_users")
    private Integer newUsers = 0;

    @Column(name = "page_views")
    private Integer pageViews = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
