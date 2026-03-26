package org.buglaban.travelapi.model;

// REVIEW ENTITY - Đánh giá tour

import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.ReviewStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends AbstractEntity {

    // Quan hệ Many-to-One với Tour
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    // Quan hệ Many-to-One với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ Many-to-One với Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5 stars

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images; // JSON array of image URLs

    @Column(name = "replied_at")
    private LocalDateTime repliedAt;

    // Validation methods
    @PrePersist
    @PreUpdate
    private void validateRatings() {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
