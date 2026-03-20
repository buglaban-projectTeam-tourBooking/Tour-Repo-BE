package org.buglaban.travelapi.dto.request.tour;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255, message = "Tên tour tối đa 255 ký tự")
    private String tourName;

    @NotBlank(message = "Mô tả ngắn không được để trống")
    @Size(max = 500, message = "Mô tả ngắn tối đa 500 ký tự")
    private String shortDescription;

    @NotBlank(message = "Mô tả chi tiết không được để trống")
    private String description;

    @NotBlank(message = "Điểm đến không được để trống")
    private String destination;

    private List<String> subDestinations;

    @NotNull(message = "Duration không được để trống")
    @Min(value = 1, message = "Duration phải > 0")
    private Integer duration;

    @NotNull(message = "Số đêm không được để trống")
    @Min(value = 0, message = "Số đêm phải >= 0")
    private Integer nights;

    @NotNull(message = "Giá người lớn không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải > 0")
    private BigDecimal adultPrice;

    @DecimalMin(value = "0.0", message = "Giá trẻ em phải >= 0")
    private BigDecimal childPrice;

    @DecimalMin(value = "0.0", message = "Giá em bé phải >= 0")
    private BigDecimal infantPrice;

    @DecimalMin(value = "0.0", message = "Giá gốc phải >= 0")
    private BigDecimal originalPrice;

    @Min(value = 0, message = "Discount >= 0")
    @Max(value = 100, message = "Discount <= 100")
    private Integer discountPercent;

    @Pattern(
            regexp = "^(http|https)://.*\\.(jpg|jpeg|png|webp)$",
            message = "Image URL phải hợp lệ (.jpg, .png, .webp)"
    )
    private String imageUrl;

    @Size(max = 20, message = "Không quá 20 ảnh gallery")
    private List<
            @Pattern(
                    regexp = "^(http|https)://.*\\.(jpg|jpeg|png|webp)$",
                    message = "Gallery URL phải hợp lệ"
            ) String
            > galleryImages;

    @Pattern(
            regexp = "^(http|https)://.*",
            message = "Video URL không hợp lệ"
    )
    private String videoUrl;

    @NotNull(message = "Category không được để trống")
    private Long categoryId;

    @Pattern(
            regexp = "^(ACTIVE|INACTIVE)$",
            message = "Status chỉ nhận ACTIVE hoặc INACTIVE"
    )
    private String status;
}
