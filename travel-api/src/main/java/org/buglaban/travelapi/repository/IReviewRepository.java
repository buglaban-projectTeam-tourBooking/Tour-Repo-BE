package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByTourIdAndStatus(Long tourId, String status, Pageable pageable);

    Page<Review> findByTourId(Long tourId, Pageable pageable);

    Page<Review> findByTourIdAndStatusAndRating(Long tourId, String status, Integer rating, Pageable pageable);

    Page<Review> findByUserId(Long userId, Pageable pageable);

    Page<Review> findByStatus(String status, Pageable pageable);

    boolean existsByUserIdAndTourId(Long userId, Long tourId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Review r WHERE r.user.id = :userId AND r.tour.id = :tourId AND r.order IS NOT NULL")
    boolean hasUserPurchasedTour(@Param("userId") Long userId, @Param("tourId") Long tourId);

    Optional<Review> findByUserIdAndTourId(Long userId, Long tourId);

    long countByTourIdAndStatus(Long tourId, String status);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tour.id = :tourId AND r.status = 'APPROVED'")
    Double calculateAverageRating(@Param("tourId") Long tourId);


    @Query("SELECT r.rating, COUNT(r) FROM Review r " +
            "WHERE r.tour.id = :tourId AND r.status = 'APPROVED' " +
            "GROUP BY r.rating ORDER BY r.rating")
    List<Object[]> getRatingDistribution(@Param("tourId") Long tourId);

    @Query("SELECT r FROM Review r WHERE r.tour.id = :tourId AND r.status = 'APPROVED' " +
            "ORDER BY r.rating DESC, r.createdAt DESC")
    List<Review> findTopReviews(@Param("tourId") Long tourId, Pageable pageable);
}