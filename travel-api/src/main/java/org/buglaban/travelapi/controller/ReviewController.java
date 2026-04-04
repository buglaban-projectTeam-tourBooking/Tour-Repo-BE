package org.buglaban.travelapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.service.IReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.buglaban.travelapi.dto.request.review.ReviewRequestDTO;
import org.buglaban.travelapi.dto.response.ResponseData;
import org.buglaban.travelapi.dto.response.ResponseFailure;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/review/")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final IReviewService reviewService;

    // -------------------------------------------------------------------------
    // PUBLIC ENDPOINTS
    // -------------------------------------------------------------------------

    /**
     * GET /reviews/tour/{tourId}
     * Lấy danh sách reviews đã duyệt của một tour (phân trang, lọc theo rating)
     */
    @GetMapping("/tour/{tourId}")
    public ResponseData<?> getTourReviews(
            @PathVariable Long tourId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false)    Integer rating,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        try {
            var result = reviewService.getTourReviews(tourId, page, pageSize, rating, sortBy);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            log.error("Error getting tour reviews: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * GET /reviews/tour/{tourId}/statistics
     * Thống kê rating của tour (avg, phân bổ 1-5 sao)
     */
    @GetMapping("/tour/{tourId}/statistics")
    public ResponseData<?> getReviewStatistics(@PathVariable Long tourId) {
        try {
            var result = reviewService.getReviewStatistics(tourId);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            log.error("Error getting review statistics: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * GET /reviews/user/{userId}
     * Lấy toàn bộ reviews của một user
     */
    @GetMapping("/user/{userId}")
    public ResponseData<?> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            var result = reviewService.getUserReviews(userId, page, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            log.error("Error getting user reviews: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * GET /reviews/{id}
     * Lấy chi tiết một review
     */
    @GetMapping("/{id}")
    public ResponseData<?> getReviewDetail(@PathVariable Long id) {
        try {
            var result = reviewService.getReviewDetail(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            log.error("Error getting review detail: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // USER ENDPOINTS
    // -------------------------------------------------------------------------

    /**
     * POST /reviews
     * Tạo review mới (user phải đã mua tour)
     */
    @PostMapping
    public ResponseData<?> createReview(@RequestBody ReviewRequestDTO requestDTO) {
        try {
            Long id = reviewService.createReview(requestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Review created successfully", id);
        } catch (Exception e) {
            log.error("Error creating review: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * PUT /reviews/{id}
     * Cập nhật review (chỉ chủ review được sửa, reset về PENDING)
     */
    @PutMapping("/{id}")
    public ResponseData<?> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequestDTO requestDTO) {
        try {
            reviewService.updateReview(id, requestDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Review updated successfully", null);
        } catch (Exception e) {
            log.error("Error updating review: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * DELETE /reviews/{id}
     * Xóa review và cập nhật lại rating tour
     */
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Review deleted successfully", null);
        } catch (Exception e) {
            log.error("Error deleting review: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // ADMIN ENDPOINTS
    // -------------------------------------------------------------------------

    /**
     * GET /reviews/admin/pending
     * Lấy danh sách reviews chờ duyệt (PENDING)
     */
    @GetMapping("/admin/pending")
    public ResponseData<?> getPendingReviews(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        try {
            var result = reviewService.getPendingReviews(page, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            log.error("Error getting pending reviews: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * PATCH /reviews/admin/{id}/status
     * Duyệt hoặc từ chối review, kèm reply của admin
     *
     * Request body:
     * {
     *   "status": "APPROVED" | "REJECTED",
     *   "adminReply": "..."   (tuỳ chọn)
     * }
     */
    @PatchMapping("/admin/{id}/status")
    public ResponseData<?> updateReviewStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String adminReply) {
        try {
            reviewService.updateReviewStatus(id, status, adminReply);
            return new ResponseData<>(HttpStatus.OK.value(), "Review status updated successfully", null);
        } catch (Exception e) {
            log.error("Error updating review status: {}", e.getMessage());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
