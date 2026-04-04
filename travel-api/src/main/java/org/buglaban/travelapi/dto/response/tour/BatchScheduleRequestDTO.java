package org.buglaban.travelapi.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO để tạo nhiều TourSchedule cùng lúc.
 *
 * Dùng cho:
 *   POST /tour-schedules/batch → TourScheduleService.createBatchSchedules()
 *
 * Logic service:
 *   for each date in departureDates:
 *       returnDate = departureDate + durationDays - 1
 *       → tạo 1 TourSchedule với availableSeats, priceAdjustment chung
 *
 * Ví dụ JSON gửi lên:
 * {
 *   "tourId": 5,
 *   "departureDates": [
 *     "2025-12-01",
 *     "2025-12-15",
 *     "2026-01-05"
 *   ],
 *   "durationDays":   4,
 *   "availableSeats": 20,
 *   "priceAdjustment": null
 * }
 *
 * → Kết quả: tạo 3 schedules với returnDate lần lượt là
 *   2025-12-04, 2025-12-18, 2026-01-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchScheduleRequestDTO {

    /** ID của tour cần tạo schedules */
    private Long tourId;

    /**
     * Danh sách ngày khởi hành – định dạng yyyy-MM-dd.
     * Mỗi phần tử tạo ra một TourSchedule riêng.
     * Phải có ít nhất 1 phần tử.
     */
    private List<String> departureDates;

    /**
     * Số ngày của tour (bao gồm ngày đầu và ngày cuối).
     * returnDate = departureDate + durationDays - 1.
     * Ví dụ: 4N3Đ → durationDays = 4.
     */
    private Integer durationDays;

    /**
     * Số chỗ áp dụng đồng đều cho tất cả schedules trong batch.
     * Mỗi schedule có thể được chỉnh riêng sau khi tạo qua PUT.
     */
    private Integer availableSeats;

    /**
     * Chênh lệch giá áp dụng đồng đều cho tất cả schedules.
     * Null = giữ nguyên basePrice của tour.
     */
    private BigDecimal priceAdjustment;
}
