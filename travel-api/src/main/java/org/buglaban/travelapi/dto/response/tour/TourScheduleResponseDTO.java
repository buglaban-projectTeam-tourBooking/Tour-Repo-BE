package org.buglaban.travelapi.dto.response.tour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourScheduleResponseDTO {
    /** ID của schedule */
    private Long id;

    /** ID của tour */
    private Long tourId;

    /** Tên tour – lấy từ tour.getTourName() */
    private String tourName;

    /** Ngày khởi hành (yyyy-MM-dd) */
    private String departureDate;

    /** Ngày kết thúc (yyyy-MM-dd) */
    private String returnDate;

    /** Tổng số chỗ của schedule */
    private Integer availableSeats;

    /** Số chỗ đã đặt */
    private Integer bookedSeats;

    /**
     * Số chỗ còn lại = availableSeats - bookedSeats.
     * Tính thêm ở đây để client không phải tự trừ.
     */
    private Integer remainingSeats;

    /**
     * Chênh lệch giá so với basePrice của tour.
     * Null = không điều chỉnh giá.
     * Dương = phụ thu, âm = giảm giá.
     */
    private BigDecimal priceAdjustment;

    /** Trạng thái: AVAILABLE | FULL | CANCELLED */
    private String status;

    /** Ghi chú nội bộ (tuỳ chọn) */
    private String note;
}
