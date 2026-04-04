package org.buglaban.travelapi.service.impl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.request.tour.TourScheduleRequestDTO;
import org.buglaban.travelapi.dto.response.*;
import org.buglaban.travelapi.dto.response.tour.AvailabilityResponseDTO;
import org.buglaban.travelapi.dto.response.tour.BatchScheduleRequestDTO;
import org.buglaban.travelapi.dto.response.tour.TourScheduleResponseDTO;
import org.buglaban.travelapi.model.Tour;
import org.buglaban.travelapi.model.TourSchedule;
import org.buglaban.travelapi.repository.ITourRepository;
import org.buglaban.travelapi.repository.ITourScheduleRepository;

import org.buglaban.travelapi.service.ITourScheduleService;
import org.buglaban.travelapi.util.ScheduleStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TourScheduleService implements ITourScheduleService {

    private final ITourScheduleRepository scheduleRepository;
    private final ITourRepository tourRepository;

    /**
     * Lấy schedules theo tourId
     */
    @Override
    public List<TourScheduleResponseDTO> getTourSchedules(
            Long tourId, LocalDate startDate, LocalDate endDate, String status) {
        log.info("Getting schedules for tour: {}", tourId);

        List<TourSchedule> schedules;

        if (startDate != null && endDate != null) {
            schedules = scheduleRepository.findByTourIdAndDateRange(tourId, startDate, endDate);
        } else if (status != null) {
            schedules = scheduleRepository.findByTourIdAndStatus(tourId, status);
        } else {
            schedules = scheduleRepository.findByTourId(tourId);
        }

        return schedules.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy schedules theo ngày khởi hành
     */
    @Override
    public List<TourScheduleResponseDTO> getSchedulesByDepartureDate(LocalDate date) {
        log.info("Getting schedules for date: {}", date);

        List<TourSchedule> schedules = scheduleRepository
                .findByDepartureDateAndStatus(date, "AVAILABLE");

        return schedules.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy schedules còn chỗ
     */
    @Override
    public List<TourScheduleResponseDTO> getAvailableSchedules(
            Long tourId, LocalDate fromDate, int participants) {
        log.info("Getting available schedules from: {}", fromDate);

        List<TourSchedule> schedules = scheduleRepository
                .findAvailableSchedules(participants, fromDate);

        if (tourId != null) {
            schedules = schedules.stream()
                    .filter(s -> s.getTour().getId().equals(tourId))
                    .collect(Collectors.toList());
        }

        return schedules.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long createSchedule(TourScheduleRequestDTO requestDTO) {
        log.info("Creating schedule for tour: {}", requestDTO.getTourId());

        Tour tour = tourRepository.findById(requestDTO.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        LocalDate departureDate = LocalDate.parse(requestDTO.getDepartureDate());
        LocalDate returnDate = LocalDate.parse(requestDTO.getReturnDate());

        if (returnDate.isBefore(departureDate)) {
            throw new RuntimeException("Return date must be after departure date");
        }

        TourSchedule schedule = new TourSchedule();
        schedule.setTour(tour);
        schedule.setDepartureDate(departureDate);
        schedule.setReturnDate(returnDate);
        schedule.setAvailableSeats(requestDTO.getAvailableSeats());
        schedule.setNote(requestDTO.getNote());
        schedule.setStatus(ScheduleStatus.valueOf(requestDTO.getStatus() != null ? requestDTO.getStatus() : ScheduleStatus.AVAILABLE.name()));

        TourSchedule saved = scheduleRepository.save(schedule);
        return saved.getId();
    }

    /**
     * Batch create schedules
     */
    @Override
    public List<Long> createBatchSchedules(BatchScheduleRequestDTO requestDTO) {
        log.info("Batch creating schedules for tour: {}", requestDTO.getTourId());

        Tour tour = tourRepository.findById(requestDTO.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        List<Long> createdIds = new ArrayList<>();

        for (String dateStr : requestDTO.getDepartureDates()) {
            LocalDate departureDate = LocalDate.parse(dateStr);
            LocalDate returnDate = departureDate.plusDays(requestDTO.getDurationDays() - 1);

            TourSchedule schedule = new TourSchedule();
            schedule.setTour(tour);
            schedule.setDepartureDate(departureDate);
            schedule.setReturnDate(returnDate);
            schedule.setAvailableSeats(requestDTO.getAvailableSeats());
            schedule.setStatus(ScheduleStatus.AVAILABLE);

            TourSchedule saved = scheduleRepository.save(schedule);
            createdIds.add(saved.getId());
        }

        log.info("Created {} schedules", createdIds.size());
        return createdIds;
    }

    /**
     * Cập nhật schedule
     */
    @Override
    public void updateSchedule(Long id, TourScheduleRequestDTO requestDTO) {
        log.info("Updating schedule: {}", id);

        TourSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setDepartureDate(LocalDate.parse(requestDTO.getDepartureDate()));
        schedule.setReturnDate(LocalDate.parse(requestDTO.getReturnDate()));
        schedule.setAvailableSeats(requestDTO.getAvailableSeats());
        schedule.setNote(requestDTO.getNote());

        scheduleRepository.save(schedule);
    }

    /**
     * Xóa schedule (chỉ khi chưa có booking)
     */
    @Override
    public void deleteSchedule(Long id) {
        log.info("Deleting schedule: {}", id);

        TourSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (schedule.getBookedSeats() > 0) {
            throw new RuntimeException("Cannot delete schedule with existing bookings");
        }

        scheduleRepository.delete(schedule);
    }

    /**
     * Check availability
     */
    @Override
    public AvailabilityResponseDTO checkAvailability(Long scheduleId, int participants) {
        TourSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        AvailabilityResponseDTO response = new AvailabilityResponseDTO();
        response.setScheduleId(scheduleId);
        response.setAvailableSeats(schedule.getRemainingSeats());
        response.setRequestedSeats(participants);
        response.setAvailable(schedule.hasAvailableSeats(participants));
        response.setMessage(response.getAvailable() ?
                "Seats available" : "Not enough seats");

        return response;
    }

    private TourScheduleResponseDTO mapToResponseDTO(TourSchedule schedule) {
        TourScheduleResponseDTO dto = new TourScheduleResponseDTO();
        dto.setId(schedule.getId());
        dto.setTourId(schedule.getTour().getId());
        dto.setTourName(schedule.getTour().getTourName());
        dto.setDepartureDate(schedule.getDepartureDate().toString());
        dto.setReturnDate(schedule.getReturnDate().toString());
        dto.setAvailableSeats(schedule.getAvailableSeats());
        dto.setBookedSeats(schedule.getBookedSeats());
        dto.setStatus(String.valueOf(schedule.getStatus()));
        dto.setNote(schedule.getNote());
        return dto;
    }
}