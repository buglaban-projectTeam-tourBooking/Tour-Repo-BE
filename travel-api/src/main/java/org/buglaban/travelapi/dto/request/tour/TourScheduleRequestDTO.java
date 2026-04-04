package org.buglaban.travelapi.dto.request.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO để tạo hoặc cập nhật một TourSchedule.
 *
 * Dùng cho:
 *   POST  /tour-schedules          → TourScheduleService.createSchedule()
 *   PUT   /tour-schedules/{id}     → TourScheduleService.updateSchedule()
 *
 * Ví dụ JSON gửi lên:
 * {
 *   "tourId":         5,
 *   "departureDate":  "2025-12-01",
 *   "returnDate":     "2025-12-04",
 *   "availableSeats": 20,
 *   "priceAdjustment": null,
 *   "note": "Lịch Tết Dương lịch",
 *   "status": "AVAILABLE"
 * }
 *
 * Lưu ý khi UPDATE: tourId không bắt buộc (schedule đã biết tour),
 * nhưng các trường còn lại đều được cập nhật.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourScheduleRequestDTO {

    /** ID của tour (bắt buộc khi CREATE, bỏ qua khi UPDATE) */
    private Long tourId;

    /**
     * Ngày khởi hành – định dạng yyyy-MM-dd.
     * Service sẽ parse bằng LocalDate.parse().
     */
    private String departureDate;

    /**
     * Ngày kết thúc – định dạng yyyy-MM-dd.
     * Phải >= departureDate, service validate trước khi lưu.
     * Khi dùng Batch thì returnDate được tự động tính
     * (departureDate + durationDays - 1), không cần truyền field này.
     */
    private String returnDate;

    /** Tổng số chỗ cho schedule này */
    private Integer availableSeats;

    /**
     * Chênh lệch giá so với basePrice của tour.
     * Null = giữ nguyên basePrice.
     * Ví dụ: +500000 = phụ thu mùa cao điểm, -200000 = ưu đãi.
     */
    private BigDecimal priceAdjustment;

    /** Ghi chú nội bộ (tuỳ chọn) */
    private String note;

    /**
     * Trạng thái ban đầu: AVAILABLE | CANCELLED.
     * Null → service mặc định là AVAILABLE.
     * FULL không được set thủ công – hệ thống tự set qua @PreUpdate.
     */
    private String status;
}
