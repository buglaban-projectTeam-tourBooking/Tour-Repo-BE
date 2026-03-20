package org.buglaban.travelapi.dto.response.tour;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDayDTO {
    private Long id;
    private String dayNumber;
    private String dayTitle;
    private String dep;
    private String description;
    private String image;
}
