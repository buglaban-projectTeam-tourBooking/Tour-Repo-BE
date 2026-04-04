package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.TourSchedule;
import org.buglaban.travelapi.util.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface ITourScheduleRepository extends JpaRepository <TourSchedule, Long> {
    Long countByTourIdAndStatus(Long id, ScheduleStatus scheduleStatus);

    List<TourSchedule> findByTourId(Long tourId);

    List<TourSchedule> findByTourIdAndStatus(Long tourId, String status);

    @Query("SELECT ts FROM TourSchedule ts WHERE ts.tour.id = :tourId " +
            "AND ts.departureDate BETWEEN :startDate AND :endDate " +
            "ORDER BY ts.departureDate ASC")
    List<TourSchedule> findByTourIdAndDateRange(
            @Param("tourId") Long tourId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<TourSchedule> findByDepartureDateAndStatus(LocalDate departureDate, String status);

    @Query("SELECT ts FROM TourSchedule ts WHERE ts.status = 'AVAILABLE' " +
            "AND (ts.availableSeats - ts.bookedSeats) >= :requiredSeats " +
            "AND ts.departureDate >= :fromDate " +
            "ORDER BY ts.departureDate ASC")
    List<TourSchedule> findAvailableSchedules(
            @Param("requiredSeats") int requiredSeats,
            @Param("fromDate") LocalDate fromDate
    );

    @Query("SELECT ts FROM TourSchedule ts WHERE ts.status = 'AVAILABLE' " +
            "AND ts.departureDate >= :today " +
            "AND ts.departureDate <= :endDate " +
            "ORDER BY ts.departureDate ASC")
    Page<TourSchedule> findUpcomingSchedules(
            @Param("today") LocalDate today,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT CASE WHEN COUNT(ts) > 0 THEN true ELSE false END " +
            "FROM TourSchedule ts WHERE ts.tour.id = :tourId " +
            "AND ts.departureDate = :departureDate " +
            "AND ts.id != :excludeId")
    boolean existsConflict(
            @Param("tourId") Long tourId,
            @Param("departureDate") LocalDate departureDate,
            @Param("excludeId") Long excludeId
    );
}
