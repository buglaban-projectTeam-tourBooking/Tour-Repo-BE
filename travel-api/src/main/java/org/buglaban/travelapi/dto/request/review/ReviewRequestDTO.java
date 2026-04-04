package org.buglaban.travelapi.dto.request.review;

import lombok.Data;

import java.util.List;
@Data
public class ReviewRequestDTO {
    private Long tourId;
    private Long userId;
    private Long orderId;

    private Integer rating;
    private Integer locationRating;
    private Integer serviceRating;
    private Integer priceRating;

    private String title;
    private String comment;

    // Danh sách ảnh (FE gửi lên)
    private List<String> images;
}
