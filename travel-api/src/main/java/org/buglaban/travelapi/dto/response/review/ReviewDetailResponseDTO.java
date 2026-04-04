package org.buglaban.travelapi.dto.response.review;

import lombok.Data;

import java.util.List;

@Data
public class ReviewDetailResponseDTO {

    private Long id;

    private Long tourId;
    private String tourName;

    private Long userId;
    private String userName;
    private String userAvatar;

    private Integer rating;
    private Integer locationRating;
    private Integer serviceRating;
    private Integer priceRating;

    private String title;
    private String comment;

    private List<String> images;

    private Boolean isVerifiedPurchase;
    private String status;

    private String adminReply;

    private String createdAt;
    private String updatedAt;
}
