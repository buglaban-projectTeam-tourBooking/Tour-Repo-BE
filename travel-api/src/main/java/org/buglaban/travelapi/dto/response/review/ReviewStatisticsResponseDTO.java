package org.buglaban.travelapi.dto.response.review;

import lombok.Data;

import java.util.Map;

@Data
public class ReviewStatisticsResponseDTO {

    private Long tourId;

    private Integer totalReviews;

    private Double averageRating;

    // key: rating (1-5), value: số lượng
    private Map<Integer, Integer> ratingDistribution;
}
