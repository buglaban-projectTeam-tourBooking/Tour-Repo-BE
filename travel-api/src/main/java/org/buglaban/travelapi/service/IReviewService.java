package org.buglaban.travelapi.service;

import org.buglaban.travelapi.dto.request.review.ReviewRequestDTO;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.review.ReviewDetailResponseDTO;
import org.buglaban.travelapi.dto.response.review.ReviewStatisticsResponseDTO;

public interface IReviewService {

    /**
     * Tạo review mới
     */
    Long createReview(ReviewRequestDTO requestDTO);

    /**
     * Lấy reviews của tour với filter
     */
    PageResponse<?> getTourReviews(Long tourId, int page, int pageSize,
                                   Integer rating, String sortBy);

    /**
     * Lấy reviews của user
     */
    PageResponse<?> getUserReviews(Long userId, int page, int pageSize);

    /**
     * Lấy chi tiết review
     */
    ReviewDetailResponseDTO getReviewDetail(Long id);

    /**
     * Cập nhật review
     */
    void updateReview(Long id, ReviewRequestDTO requestDTO);

    /**
     * Xóa review
     */
    void deleteReview(Long id);

    /**
     * Admin duyệt/từ chối review
     */
    void updateReviewStatus(Long id, String status, String adminReply);

    /**
     * Lấy reviews chờ duyệt (Admin)
     */
    PageResponse<?> getPendingReviews(int page, int pageSize);

    /**
     * Thống kê reviews của tour
     */
    ReviewStatisticsResponseDTO getReviewStatistics(Long tourId);
}
