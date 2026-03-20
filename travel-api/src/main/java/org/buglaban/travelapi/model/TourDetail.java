package org.buglaban.travelapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tour_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetail extends AbstractEntity{
    @Column(name = "day_number", nullable = false)
    private String dayNumber;
    @Column(name = "day_title", nullable = false)
    private String dayTitle;
    @Column(name = "dep", nullable = false)
    private String dep;
    @Column(name = "description",columnDefinition = "TEXT", nullable = false)
    private String description;
    @Column(name = "image", length = 500)
    private String image;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tour_id")
    private Tour tour;
}
