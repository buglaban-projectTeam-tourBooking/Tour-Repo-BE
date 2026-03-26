package org.buglaban.travelapi.service;

import org.buglaban.travelapi.dto.response.tour.*;
import org.springframework.data.domain.Page;
import org.buglaban.travelapi.dto.request.tour.AdminTourCreateUpdateDTO;
import org.buglaban.travelapi.dto.request.tour.CalculatePriceRequestDTO;
import org.buglaban.travelapi.dto.request.tour.TourFilterRequestDTO;
import org.buglaban.travelapi.dto.request.tour.TourScheduleRequestDTO;

import java.util.List;

public interface ITourService {
    // Public
    Page<TourSummaryDTO> getPublicTours(TourFilterRequestDTO filterDTO);
    TourDetailDTO getTourBySlug(String slug);
    CalculatePriceResponseDTO calculatePrice(CalculatePriceRequestDTO requestDTO);
    List<TourSummaryDTO> getRelatedTours(Long tourId, int limit);
    Page<TourSummaryDTO> searchTours(String keyword, int page, int pageSize);

    // Admin
    Page<AdminTourSummaryDTO> getAdminTours(TourFilterRequestDTO filterDTO);
    Long createTour(AdminTourCreateUpdateDTO dto);
    void updateTour(Long id, AdminTourCreateUpdateDTO dto);
    void deleteTour(Long id);
    void deleteTours(List<Long> ids);
    void changeToursStatus(List<Long> ids, String status);
    TourDetailDTO previewTour(Long id);

    // Schedules
    Long addSchedule(Long tourId, TourScheduleRequestDTO dto);
    List<TourScheduleDTO> getSchedulesByTour(Long tourId);
}
