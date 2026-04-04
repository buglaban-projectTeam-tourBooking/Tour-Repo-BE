package org.buglaban.travelapi.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO cho endpoint kiểm tra chỗ trống.
 *
 * Được map bởi TourScheduleService.checkAvailability(scheduleId, participants).
 * Dùng cho:
 *   GET /tour-schedules/{id}/availability?participants=2
 *
 * Ví dụ JSON khi còn chỗ:
 * {
 *   "scheduleId":     1,
 *   "available":      true,
 *   "availableSeats": 12,
 *   "requestedSeats": 2,
 *   "message":        "Seats available"
 * }
 *
 * Ví dụ JSON khi hết chỗ:
 * {
 *   "scheduleId":     1,
 *   "available":      false,
 *   "availableSeats": 0,
 *   "requestedSeats": 3,
 *   "message":        "Not enough seats"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponseDTO {

    /** ID của schedule được kiểm tra */
    private Long scheduleId;

    /**
     * true  → còn đủ chỗ cho requestedSeats người.
     * false → không đủ chỗ.
     */
    private Boolean available;

    /**
     * Số chỗ còn lại thực tế = availableSeats - bookedSeats.
     * Client dùng để hiển thị "Còn X chỗ".
     */
    private Integer availableSeats;

    /** Số người khách yêu cầu (tham số participants từ request) */
    private Integer requestedSeats;

    /**
     * Thông báo ngắn gọn:
     *   "Seats available"  khi available = true
     *   "Not enough seats" khi available = false
     */
    private String message;
}
