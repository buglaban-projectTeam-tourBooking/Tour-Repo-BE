package org.buglaban.travelapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.request.*;
import org.buglaban.travelapi.dto.request.tour.AdminTourCreateUpdateDTO;
import org.buglaban.travelapi.dto.request.tour.TourFilterRequestDTO;
import org.buglaban.travelapi.dto.request.tour.TourScheduleRequestDTO;
import org.buglaban.travelapi.dto.response.*;
import org.buglaban.travelapi.dto.response.tour.TourDetailDTO;
import org.buglaban.travelapi.service.ITourService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/tours")
@RequiredArgsConstructor
@Slf4j
public class AdminTourController {

    private final ITourService tourService;
    //ok
    @GetMapping
    public ResponseData<?> getAdminTours(TourFilterRequestDTO filterDTO) {
        try {
            Page<?> result = tourService.getAdminTours(filterDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Get admin tours successfully", result);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get admin tours fail");
        }
    }

    @PostMapping
    public ResponseData<?> createTour(@RequestBody AdminTourCreateUpdateDTO dto) {
        try {
            Long id = tourService.createTour(dto);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Tour created successfully", id);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Create tour fail");
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseData<?> updateTour(@PathVariable Long id, @RequestBody AdminTourCreateUpdateDTO dto) {
        try {
            tourService.updateTour(id, dto);
            return new ResponseData<>(HttpStatus.OK.value(), "Tour updated successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Update tour fail");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteTour(@PathVariable Long id) {
        try {
            tourService.deleteTour(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Tour deleted successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Delete tour fail");
        }
    }

    @DeleteMapping("/bulk")
    public ResponseData<?> deleteTours(@RequestBody List<Long> ids) {
        try {
            tourService.deleteTours(ids);
            return new ResponseData<>(HttpStatus.OK.value(), "Tours deleted successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Delete tours fail");
        }
    }

    @PatchMapping("/bulk-status")
    public ResponseData<?> changeToursStatus(@RequestParam String status,
                                             @RequestBody List<Long> ids) {
        try {
            tourService.changeToursStatus(ids, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Status updated successfully");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Change status fail");
        }
    }

    @GetMapping("/{id}/preview")
    public ResponseData<?> previewTour(@PathVariable Long id) {
        try {
            TourDetailDTO detail = tourService.previewTour(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Preview tour successfully", detail);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Preview tour fail");
        }
    }

    @PostMapping("/{id}/schedules")
    public ResponseData<?> addSchedule(@PathVariable Long id, @RequestBody TourScheduleRequestDTO dto) {
        try {
            Long scheduleId = tourService.addSchedule(id, dto);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Schedule added successfully", scheduleId);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Add schedule fail");
        }
    }

    @GetMapping("/{id}/schedules")
    public ResponseData<?> getSchedules(@PathVariable Long id) {
        try {
            List<?> schedules = tourService.getSchedulesByTour(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get schedules successfully", schedules);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get schedules fail");
        }
    }
}