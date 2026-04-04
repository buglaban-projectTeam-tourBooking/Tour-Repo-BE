package org.buglaban.travelapi.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.buglaban.travelapi.model.Order;
import org.buglaban.travelapi.model.Tour;
import org.buglaban.travelapi.model.User;

import java.time.LocalDateTime;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Tour tour;

    private User user;

    private Order order;

    private Integer rating;

    private String title;

    private String comment;

    private String images;

    private LocalDateTime repliedAt;
}
