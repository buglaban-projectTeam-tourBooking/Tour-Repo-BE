package org.buglaban.travelapi.dto.request.tour;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDayRequestDTO {
    private String dayNumber;
    private String dayTitle;
    private String dep;
    private String description;
    private String image;
}
