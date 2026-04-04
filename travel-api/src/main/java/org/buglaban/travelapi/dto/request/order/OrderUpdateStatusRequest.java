package org.buglaban.travelapi.dto.request.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateStatusRequest {

    @NotBlank(message = "Trạng thái đơn hàng không được để trống")
    private String status;

    private String adminNote;
}