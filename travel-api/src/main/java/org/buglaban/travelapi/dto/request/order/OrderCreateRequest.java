package org.buglaban.travelapi.dto.request.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderCreateRequest {

    @NotNull(message = "Tour ID không được để trống")
    private Long tourId;

    @NotNull(message = "Tour Schedule ID không được để trống")
    private Long tourScheduleId;

    @Positive(message = "Số lượng người lớn phải lớn hơn 0")
    private int adultQuantity;

    @Positive(message = "Số lượng trẻ em phải lớn hơn hoặc bằng 0")
    private int childQuantity = 0;

    @Positive(message = "Số lượng em bé phải lớn hơn hoặc bằng 0")
    private int infantQuantity = 0;

    @Size(min = 1, message = "Phải có ít nhất một hành khách")
    private List<OrderParticipantRequest> participants;

    private String customerNote;

    // Phương thức thanh toán (ví dụ: VNPAY, MOMO, BANK_TRANSFER, CASH)
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;
}
