package org.buglaban.travelapi.service;

import org.buglaban.travelapi.dto.request.tour.TourScheduleRequestDTO;

import org.buglaban.travelapi.dto.response.tour.AvailabilityResponseDTO;
import org.buglaban.travelapi.dto.response.tour.BatchScheduleRequestDTO;
import org.buglaban.travelapi.dto.response.tour.TourScheduleResponseDTO;


import java.time.LocalDate;
import java.util.List;

public interface ITourScheduleService {

    /**
     * Lấy schedules theo tourId (có filter)
     */
    List<TourScheduleResponseDTO> getTourSchedules(
            Long tourId,
            LocalDate startDate,
            LocalDate endDate,
            String status
    );

    /**
     * Lấy schedules theo ngày khởi hành
     */
    List<TourScheduleResponseDTO> getSchedulesByDepartureDate(LocalDate date);

    /**
     * Lấy schedules còn chỗ
     */
    List<TourScheduleResponseDTO> getAvailableSchedules(
            Long tourId,
            LocalDate fromDate,
            int participants
    );

    /**
     * Tạo schedule mới
     */
    Long createSchedule(TourScheduleRequestDTO requestDTO);

    /**
     * Tạo nhiều schedule (batch)
     */
    List<Long> createBatchSchedules(org.buglaban.travelapi.dto.response.tour.BatchScheduleRequestDTO requestDTO);

    /**
     * Cập nhật schedule
     */
    void updateSchedule(Long id, TourScheduleRequestDTO requestDTO);

    /**
     * Xóa schedule
     */
    void deleteSchedule(Long id);

    /**
     * Check availability (còn chỗ không)
     */
    AvailabilityResponseDTO checkAvailability(Long scheduleId, int participants);
}