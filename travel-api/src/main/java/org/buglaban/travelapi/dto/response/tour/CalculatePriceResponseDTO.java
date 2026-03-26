package org.buglaban.travelapi.dto.response.tour;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculatePriceResponseDTO {
    private BigDecimal adultSubtotal;
    private BigDecimal childSubtotal;
    private BigDecimal infantSubtotal;
    private BigDecimal subtotal;
    private BigDecimal finalAmount;
    private int availableSlots;
    private boolean isAvailable;
}
