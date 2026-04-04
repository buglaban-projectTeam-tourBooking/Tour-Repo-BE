package org.buglaban.travelapi.model;

// TOUR ENTITY - Tour du lịch (Entity chính của hệ thống)
import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour extends AbstractEntity {

    @Column(name = "tour_code", unique = true, nullable = false, length = 50)
    private String tourCode;

    @Column(name = "tour_name", nullable = false, length = 500)
    private String tourName;

    @Column(name = "slug", unique = true, nullable = false, length = 500)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    // Thông tin địa điểm
    @Column(name = "departure_location")
    private String departureLocation;

    @Column(name = "destination")
    private String destination;

    // Thông tin thời gian
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "duration_nights", nullable = false)
    private Integer durationNights;

    // Giá tour
    @Column(name = "base_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "adult_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal adultPrice;

    @Column(name = "child_price", precision = 15, scale = 2)
    private BigDecimal childPrice;

    @Column(name = "infant_price", precision = 15, scale = 2)
    private BigDecimal infantPrice;

    // Thông tin khác
    @Column(name = "featured_image", length = 500)
    private String featuredImage;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    // Trạng thái & SEO
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TourStatus status = TourStatus.DRAFT;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "rating_average", precision = 3, scale = 2)
    private BigDecimal ratingAverage;

    @Column(name = "total_reviews")
    private Integer totalReviews;

    // Quan hệ Many-to-One với Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Quan hệ Many-to-One với User (created_by)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // Quan hệ One-to-Many với TourSchedule
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<TourSchedule> schedules = new HashSet<>();

    // Quan hệ One-to-Many với Review
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

}
