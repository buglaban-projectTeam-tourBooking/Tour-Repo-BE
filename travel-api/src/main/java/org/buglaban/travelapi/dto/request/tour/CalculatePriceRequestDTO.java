package org.buglaban.travelapi.dto.request.tour;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculatePriceRequestDTO {
    private Long tourId;
    private String slug;
    private Long scheduleId;
    private int adultQty;
    private int childQty;
    private int infantQty;
}
