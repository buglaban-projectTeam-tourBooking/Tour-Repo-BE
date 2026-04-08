package org.buglaban.travelapi.dto.request.tour;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTourDTO {

    @NotBlank(message = "Tour code không được để trống")
    @Size(max = 50, message = "Tour code tối đa 50 ký tự")
    private String tourCode;

    @NotBlank(message = "Tên tour không được để trống")
    @Size(max = 500, message = "Tên tour tối đa 500 ký tự")
    private String tourName;

    // === THÊM: Slug bắt buộc ===
    @NotBlank(message = "Slug không được để trống")
    @Size(max = 500, message = "Slug tối đa 500 ký tự")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug chỉ được chứa chữ thường, số và dấu gạch ngang")
    private String slug;

    @NotBlank(message = "Mô tả ngắn không được để trống")
    @Size(max = 500, message = "Mô tả ngắn tối đa 500 ký tự")
    private String shortDescription;

    @NotBlank(message = "Mô tả chi tiết không được để trống")
    private String description;

    @NotBlank(message = "Điểm khởi hành không được để trống")
    private String departureLocation;

    @NotBlank(message = "Điểm đến không được để trống")
    private String destination;

    private List<String> subDestinations;

    @NotNull(message = "Số ngày không được để trống")
    @Min(value = 1, message = "Số ngày phải lớn hơn 0")
    private Integer durationDays;

    @NotNull(message = "Số đêm không được để trống")
    @Min(value = 0, message = "Số đêm phải >= 0")
    private Integer durationNights;

    // Giá bắt buộc theo schema
    @NotNull(message = "Giá gốc (basePrice) không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá gốc phải > 0")
    private BigDecimal basePrice;

    @NotNull(message = "Giá người lớn không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá người lớn phải > 0")
    private BigDecimal adultPrice;

    @DecimalMin(value = "0.0", message = "Giá trẻ em phải >= 0")
    private BigDecimal childPrice;

    @DecimalMin(value = "0.0", message = "Giá em bé phải >= 0")
    private BigDecimal infantPrice;

    @Pattern(
            regexp = "^(http|https)://.*\\.(jpg|jpeg|png|webp|gif)$",
            message = "Featured Image URL phải hợp lệ"
    )
    private String featuredImage;

    @Size(max = 20, message = "Không quá 20 ảnh gallery")
    private List<String> galleryImages;

    @Pattern(
            regexp = "^(http|https)://.*",
            message = "Video URL không hợp lệ"
    )
    private String videoUrl;

    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;

    @Pattern(regexp = "^(PUBLISHED|DRAFT|INACTIVE)$",
            message = "Status chỉ nhận PUBLISHED, DRAFT hoặc INACTIVE")
    private String status = "DRAFT";

    // Lịch trình chi tiết (days)
    private List<TourDayRequestDTO> days;
}