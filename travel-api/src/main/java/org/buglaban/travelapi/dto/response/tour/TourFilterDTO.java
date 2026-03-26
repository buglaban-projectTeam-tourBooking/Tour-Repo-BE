package org.buglaban.travelapi.dto.response.tour;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.buglaban.travelapi.dto.response.CategoryDTO;
import org.buglaban.travelapi.dto.response.ReviewDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourFilterDTO {
    private Long id;

    private String tourCode;

    private String tourName;

    private String slug; //du-lich-ha-long-2n1d

    private String description; //mô tả tổng quan

    private String shortDescription;

    // Thông tin địa điểm
    private String departureLocation;

    private String destination; //'Hạ long, Quảng Ninh'

    // Thông tin thời gian
    private Integer durationDays;//2ngay

    private Integer durationNights; //1dem

    // Giá tour

    private BigDecimal adultPrice; //2,5m

    private BigDecimal childPrice; //1,7m

    private BigDecimal infantPrice;//250k

    // Thông tin khác
    private String featuredImage;

    private String videoUrl;


    // Gallery images for slider/lightbox
    private List<TourImageDTO> gallery;

    // ========== STATUS & METRICS ==========
    private String status;             // "PUBLISHED" (not shown to customer)
    private Integer viewCount;
    private Double averageRating;
    private Integer totalReviews;

    private CategoryDTO category;

    private List<ReviewDTO> reviews;

    private List<TourSummaryDTO> similarTours;  // 4-6 tours

    private List<TourSummaryDTO> relatedTours;  // 3-4 tours

    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

}
