package org.buglaban.travelapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.response.tour.TourDetailDTO;
import org.buglaban.travelapi.model.Tour;
import org.buglaban.travelapi.model.TourSchedule;
import org.buglaban.travelapi.repository.ITourRepository;
import org.buglaban.travelapi.repository.ITourScheduleRepository;

import org.buglaban.travelapi.util.ScheduleStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TourDetailService {

    private final ITourRepository tourRepository;
    private final ITourScheduleRepository scheduleRepository;

    /**
     * Lấy tour detail với join schedules, prices, description
     */
    public TourDetailDTO getTourDetailById(Long tourId) {
        log.info("Getting tour detail for ID: {}", tourId);

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        // Increment view count
        tour.setViewCount(tour.getViewCount() != null ? tour.getViewCount() + 1 : 1);
        tourRepository.save(tour);

        return buildTourDetail(tour);
    }

    public TourDetailDTO getTourDetailBySlug(String slug) {
        log.info("Getting tour detail by slug: {}", slug);

        Tour tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        // Increment view count
        tour.setViewCount(tour.getViewCount() != null ? tour.getViewCount() + 1 : 1);
        tourRepository.save(tour);

        return buildTourDetail(tour);
    }

    private TourDetailDTO buildTourDetail(Tour tour) {
        TourDetailDTO dto = new TourDetailDTO();

        // Basic info
        dto.setId(tour.getId());
        dto.setTourCode(tour.getTourCode());
        dto.setTourName(tour.getTourName());
        dto.setSlug(tour.getSlug());
        dto.setShortDescription(tour.getShortDescription());
        dto.setDescription(tour.getDescription());

        // Prices
        dto.setBasePrice(tour.getBasePrice());
        dto.setAdultPrice(tour.getAdultPrice());
        dto.setChildPrice(tour.getChildPrice());
        dto.setInfantPrice(tour.getInfantPrice());

        // Details
        dto.setDurationDays(tour.getDurationDays());
        dto.setDurationNights(tour.getDurationNights());
        dto.setDepartureLocation(tour.getDepartureLocation());
        dto.setDestination(tour.getDestination());

        // Media
        dto.setFeaturedImage(tour.getFeaturedImage());
        dto.setVideoUrl(tour.getVideoUrl());

        // Stats
        dto.setViewCount(tour.getViewCount());
        dto.setRatingAverage(tour.getRatingAverage());
        dto.setTotalReviews(Long.valueOf(tour.getTotalReviews()));

        // Category
        if (tour.getCategory() != null) {
            dto.setCategoryId(tour.getCategory().getId());
            dto.setCategoryName(tour.getCategory().getCategoryName());
        }

        // Get available schedules (next 90 days)
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(90);
        List<TourSchedule> schedules = scheduleRepository
                .findByTourIdAndDateRange(tour.getId(), today, futureDate);

        dto.setUpcomingSchedules(
                schedules.stream()
                        .filter(s -> ScheduleStatus.AVAILABLE.name().equals(s.getStatus()))
                        .map(s -> TourDetailDTO.UpcomingSchedule.builder()
                                .id(s.getId())
                                .departureDate(s.getDepartureDate().toString())
                                .returnDate(s.getReturnDate().toString())
                                .availableSeats(s.getRemainingSeats())
                                .status(String.valueOf(s.getStatus()))
                                .build()
                        )
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
