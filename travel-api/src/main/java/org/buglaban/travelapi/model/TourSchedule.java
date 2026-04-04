package org.buglaban.travelapi.model;
// TOUR SCHEDULE ENTITY - Lịch khởi hành tour

import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.ScheduleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tour_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourSchedule extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "booked_seats")
    private Integer bookedSeats = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ScheduleStatus status = ScheduleStatus.AVAILABLE;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public int getRemainingSeats() {
        return availableSeats - bookedSeats;
    }

    public boolean hasAvailableSeats(int requestedSeats) {
        return getRemainingSeats() >= requestedSeats;
    }
}
