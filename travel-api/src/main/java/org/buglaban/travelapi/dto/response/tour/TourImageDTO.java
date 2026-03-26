package org.buglaban.travelapi.dto.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class TourImageDTO {
    private String url;
    private String thumbnail;
    private String alt;
    private Integer order;
}
