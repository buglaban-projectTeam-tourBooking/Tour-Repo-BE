package org.buglaban.travelapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.request.review.ReviewRequestDTO;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.review.ReviewDetailResponseDTO;
import org.buglaban.travelapi.dto.response.review.ReviewStatisticsResponseDTO;
import org.buglaban.travelapi.model.Order;
import org.buglaban.travelapi.model.Review;
import org.buglaban.travelapi.model.Tour;
import org.buglaban.travelapi.model.User;
import org.buglaban.travelapi.repository.IOrderRepository;
import org.buglaban.travelapi.repository.IReviewRepository;
import org.buglaban.travelapi.repository.ITourRepository;
import org.buglaban.travelapi.repository.IUserRepository;
import org.buglaban.travelapi.service.IReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final ITourRepository tourRepository;
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;

    /**
     * Tạo review mới
     * - Check user đã mua tour chưa (verified purchase)
     * - Check user đã review chưa (chỉ 1 review/tour/user)
     */
    @Override
    public Long createReview(ReviewRequestDTO requestDTO) {
        log.info("Creating review for tour ID: {}", requestDTO.getTourId());

        // Validate tour exists
        Tour tour = tourRepository.findById(requestDTO.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        // Validate user exists
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already reviewed this tour
        if (reviewRepository.existsByUserIdAndTourId(requestDTO.getUserId(), requestDTO.getTourId())) {
            throw new RuntimeException("You have already reviewed this tour");
        }

        // Check if user purchased this tour (verified purchase)
        Order order = null;
        boolean isVerifiedPurchase = false;

        if (requestDTO.getOrderId() != null) {
            order = orderRepository.findById(requestDTO.getOrderId())
                    .orElse(null);
            if (order != null && order.getUser() != null
                    && order.getUser().getId().equals(requestDTO.getUserId())) {
                isVerifiedPurchase = true;
            }
        }

        // Create review
        Review review = new Review();
        review.setTour(tour);
        review.setUser(user);
        review.setOrder(order);
        review.setRating(requestDTO.getRating());
        review.setLocationRating(requestDTO.getLocationRating());
        review.setServiceRating(requestDTO.getServiceRating());
        review.setPriceRating(requestDTO.getPriceRating());
        review.setTitle(requestDTO.getTitle());
        review.setComment(requestDTO.getComment());

        // Convert image list to JSON string
        if (requestDTO.getImages() != null && !requestDTO.getImages().isEmpty()) {
            review.setImages(String.join(",", requestDTO.getImages()));
        }

        review.setIsVerifiedPurchase(isVerifiedPurchase);
        review.setStatus("PENDING"); // Chờ admin duyệt

        Review saved = reviewRepository.save(review);
        log.info("Review created successfully with ID: {}", saved.getId());

        return saved.getId();
    }

    /**
     * Lấy reviews của tour với filter
     */
    @Override
    public PageResponse<?> getTourReviews(Long tourId, int page, int pageSize,
                                          Integer rating, String sortBy) {
        log.info("Getting reviews for tour ID: {}", tourId);

        // Validate tour exists
        if (!tourRepository.existsById(tourId)) {
            throw new RuntimeException("Tour not found");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Review> reviewPage;

        if (rating != null) {
            // Filter by rating
            reviewPage = reviewRepository.findByTourIdAndStatusAndRating(
                    tourId, "APPROVED", rating, pageable);
        } else {
            // Get all approved reviews
            reviewPage = reviewRepository.findByTourIdAndStatus(
                    tourId, "APPROVED", pageable);
        }

        List<ReviewDetailResponseDTO> reviews = reviewPage.getContent().stream()
                .map(this::mapToDetailDTO)
                .toList();

        return PageResponse.builder()
                .page(page)
                .pageSize(pageSize)
                .totalPage(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .items(Collections.singletonList(reviews))
                .build();
    }

    /**
     * Lấy reviews của user
     */
    @Override
    public PageResponse<?> getUserReviews(Long userId, int page, int pageSize) {
        log.info("Getting reviews for user ID: {}", userId);

        Pageable pageable = PageRequest.of(page, pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Review> reviewPage = reviewRepository.findByUserId(userId, pageable);

        List<ReviewDetailResponseDTO> reviews = reviewPage.getContent().stream()
                .map(this::mapToDetailDTO)
                .toList();

        return PageResponse.builder()
                .page(page)
                .pageSize(pageSize)
                .totalPage(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .items(Collections.singletonList(reviews))
                .build();
    }

    /**
     * Lấy chi tiết review
     */
    @Override
    public ReviewDetailResponseDTO getReviewDetail(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        return mapToDetailDTO(review);
    }

    /**
     * Cập nhật review (chỉ user tạo review mới được sửa)
     */
    @Override
    public void updateReview(Long id, ReviewRequestDTO requestDTO) {
        log.info("Updating review ID: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Check ownership
        if (!review.getUser().getId().equals(requestDTO.getUserId())) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        // Update fields
        review.setRating(requestDTO.getRating());
        review.setLocationRating(requestDTO.getLocationRating());
        review.setServiceRating(requestDTO.getServiceRating());
        review.setPriceRating(requestDTO.getPriceRating());
        review.setTitle(requestDTO.getTitle());
        review.setComment(requestDTO.getComment());

        if (requestDTO.getImages() != null && !requestDTO.getImages().isEmpty()) {
            review.setImages(String.join(",", requestDTO.getImages()));
        }

        // Reset to PENDING khi sửa
        review.setStatus("PENDING");

        reviewRepository.save(review);
        log.info("Review updated successfully");
    }

    /**
     * Xóa review
     */
    @Override
    public void deleteReview(Long id) {
        log.info("Deleting review ID: {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewRepository.delete(review);

        // Cập nhật lại rating của tour
        updateTourRating(review.getTour().getId());

        log.info("Review deleted successfully");
    }

    /**
     * Admin duyệt/từ chối review
     */
    @Override
    public void updateReviewStatus(Long id, String status, String adminReply) {
        log.info("Updating review status to: {}", status);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus(status);

        if (adminReply != null && !adminReply.isEmpty()) {
            review.setAdminReply(adminReply);
            review.setRepliedAt(LocalDateTime.now());
        }

        reviewRepository.save(review);

        // Nếu APPROVED, cập nhật rating tour
        if ("APPROVED".equals(status)) {
            updateTourRating(review.getTour().getId());
        }

        log.info("Review status updated successfully");
    }

    /**
     * Lấy reviews chờ duyệt (Admin)
     */
    @Override
    public PageResponse<?> getPendingReviews(int page, int pageSize) {
        log.info("Getting pending reviews");

        Pageable pageable = PageRequest.of(page, pageSize,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        Page<Review> reviewPage = reviewRepository.findByStatus("PENDING", pageable);

        List<ReviewDetailResponseDTO> reviews = reviewPage.getContent().stream()
                .map(this::mapToDetailDTO)
                .toList();

        return PageResponse.builder()
                .page(page)
                .pageSize(pageSize)
                .totalPage(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .items(Collections.singletonList(reviews))
                .build();
    }

    /**
     * Thống kê reviews của tour
     */
    @Override
    public ReviewStatisticsResponseDTO getReviewStatistics(Long tourId) {
        log.info("Getting review statistics for tour ID: {}", tourId);

        // Validate tour
        if (!tourRepository.existsById(tourId)) {
            throw new RuntimeException("Tour not found");
        }

        ReviewStatisticsResponseDTO stats = new ReviewStatisticsResponseDTO();
        stats.setTourId(tourId);

        // Total reviews
        long totalReviews = reviewRepository.countByTourIdAndStatus(tourId, "APPROVED");
        stats.setTotalReviews((int) totalReviews);

        // Average rating
        Double avgRating = reviewRepository.calculateAverageRating(tourId);
        stats.setAverageRating(avgRating != null ? avgRating : 0.0);

        // Rating distribution
        List<Object[]> distribution = reviewRepository.getRatingDistribution(tourId);
        Map<Integer, Integer> ratingMap = new HashMap<>();
        for (Object[] row : distribution) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            ratingMap.put(rating, count.intValue());
        }
        stats.setRatingDistribution(ratingMap);

        return stats;
    }

    /**
     * Cập nhật rating trung bình của tour
     */
    private void updateTourRating(Long tourId) {
        Double avgRating = reviewRepository.calculateAverageRating(tourId);
        long totalReviews = reviewRepository.countByTourIdAndStatus(tourId, "APPROVED");

        Tour tour = tourRepository.findById(tourId).orElse(null);
        if (tour != null) {
            tour.setRatingAverage(avgRating != null ?
                    java.math.BigDecimal.valueOf(avgRating) : null);
            tour.setTotalReviews((int) totalReviews);
            tourRepository.save(tour);
            log.info("Updated tour rating: avg={}, total={}", avgRating, totalReviews);
        }
    }

    /**
     * Map Review entity to DTO
     */
    private ReviewDetailResponseDTO mapToDetailDTO(Review review) {
        ReviewDetailResponseDTO dto = new ReviewDetailResponseDTO();
        dto.setId(review.getId());
        dto.setTourId(review.getTour().getId());
        dto.setTourName(review.getTour().getTourName());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getFullName());
        dto.setUserAvatar(review.getUser().getAvatarUrl());
        dto.setRating(review.getRating());
        dto.setLocationRating(review.getLocationRating());
        dto.setServiceRating(review.getServiceRating());
        dto.setPriceRating(review.getPriceRating());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());

        // Parse images
        if (review.getImages() != null && !review.getImages().isEmpty()) {
            dto.setImages(List.of(review.getImages().split(",")));
        }

        dto.setIsVerifiedPurchase(review.getIsVerifiedPurchase());
        dto.setStatus(review.getStatus());
        dto.setAdminReply(review.getAdminReply());
        dto.setCreatedAt(review.getCreatedAt().toString());
        dto.setUpdatedAt(review.getUpdatedAt().toString());

        return dto;
    }
}
