package org.buglaban.travelapi.dto.response.tour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buglaban.travelapi.util.TourStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO đầy đủ cho TourDetail.
 *
 * Được map bởi TourDetailService.buildTourDetail(Tour).
 * Dùng cho:
 *   GET /tours/{id}/detail
 *   GET /tours/slug/{slug}/detail
 *
 * Ví dụ JSON trả về:
 * {
 *   "id": 5,
 *   "tourCode": "DN-4N3D-001",
 *   "tourName": "Tour Đà Nẵng 4N3Đ",
 *   "slug": "tour-da-nang-4n3d",
 *   "shortDescription": "Khám phá Đà Nẵng...",
 *   "description": "<p>Nội dung đầy đủ...</p>",
 *   "basePrice": 3500000,
 *   "adultPrice": 3500000,
 *   "childPrice": 2500000,
 *   "infantPrice": 500000,
 *   "durationDays": 4,
 *   "durationNights": 3,
 *   "departureLocation": "Hà Nội",
 *   "destination": "Đà Nẵng",

 *   "featuredImage": "https://...",
 *   "videoUrl": null,
 *   "viewCount": 1024,
 *   "ratingAverage": 4.7,
 *   "totalReviews": 58,
 *   "categoryId": 2,
 *   "categoryName": "Tour trong nước",
 *   "upcomingSchedules": [
 *     { "id": 1, "departureDate": "2025-12-01", "returnDate": "2025-12-04",
 *       "availableSeats": 12, "priceAdjustment": null, "status": "AVAILABLE" },
 *     { "id": 2, "departureDate": "2025-12-15", "returnDate": "2025-12-18",
 *       "availableSeats": 20, "priceAdjustment": 500000, "status": "AVAILABLE" }
 *   ]
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetailDTO {

    private Long id;

    /** Mã tour nội bộ, ví dụ: DN-4N3D-001 */
    private String tourCode;

    private String tourName;

    /** Slug SEO-friendly, ví dụ: tour-da-nang-4n3d */
    private String slug;

    /** Mô tả ngắn – dùng cho card/listing */
    private String shortDescription;

    /** Mô tả đầy đủ – có thể là HTML */
    private String description;

    /** Giá gốc / giá người lớn cơ bản */
    private BigDecimal basePrice;

    /** Giá người lớn (có thể bằng basePrice) */
    private BigDecimal adultPrice;

    private TourStatus status;
    /** Giá trẻ em */
    private BigDecimal childPrice;

    /** Giá em bé (thường rất thấp hoặc 0) */
    private BigDecimal infantPrice;

    /** Số ngày (ví dụ: 4 cho 4N3Đ) */
    private Integer durationDays;

    /** Số đêm (ví dụ: 3 cho 4N3Đ) */
    private Integer durationNights;



    /** Điểm khởi hành, ví dụ: Hà Nội, TP.HCM */
    private String departureLocation;

    /** Điểm đến, ví dụ: Đà Nẵng */
    private String destination;


    /** URL ảnh đại diện */
    private String featuredImage;

    /** URL video giới thiệu (tuỳ chọn) */
    private String videoUrl;

    /** Số lượt xem – tự động tăng mỗi lần gọi API này */
    private Integer viewCount;

    /** Điểm đánh giá trung bình (1.0 – 5.0) */
    private BigDecimal ratingAverage;

    private Long categoryId;

    private String categoryName;

    /**
     * Danh sách schedules AVAILABLE trong 90 ngày kể từ hôm nay.
     * Được lấy từ TourScheduleRepository.findByTourIdAndDateRange().
     * Client dùng để hiển thị lịch chọn ngày trên trang chi tiết tour.
     */
    private List<UpcomingSchedule> upcomingSchedules;

    private String categorySlug;
    private List<TourDayDTO> itinerary;
    private List<TourScheduleDTO> schedules;
    private Double averageRating;
    private Long totalReviews;

    /**
     * Inner DTO cho từng schedule trong danh sách upcoming.
     * Chỉ chứa thông tin cần thiết để khách chọn ngày,
     * không lặp lại toàn bộ TourScheduleResponseDTO.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpcomingSchedule {

        /** ID của schedule – dùng để đặt tour */
        private Long id;

        /** Ngày khởi hành (yyyy-MM-dd) */
        private String departureDate;

        /** Ngày kết thúc (yyyy-MM-dd) */
        private String returnDate;

        /**
         * Số chỗ còn lại = availableSeats - bookedSeats.
         * Từ TourSchedule.getRemainingSeats().
         */
        private Integer availableSeats;


        /** Luôn là AVAILABLE (đã filter trong buildTourDetail) */
        private String status;
    }
}
