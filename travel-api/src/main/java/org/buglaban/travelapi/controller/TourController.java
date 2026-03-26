package org.buglaban.travelapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.request.*;
import org.buglaban.travelapi.dto.request.tour.CalculatePriceRequestDTO;
import org.buglaban.travelapi.dto.request.tour.TourFilterRequestDTO;
import org.buglaban.travelapi.dto.response.*;
import org.buglaban.travelapi.dto.response.tour.CalculatePriceResponseDTO;
import org.buglaban.travelapi.dto.response.tour.TourDetailDTO;
import org.buglaban.travelapi.service.ITourService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/tours")
@RequiredArgsConstructor
@Slf4j
public class TourController {

    private final ITourService tourService;

    @GetMapping
    public ResponseData<?> getPublicTours(TourFilterRequestDTO filterDTO) {
        try {
            Page<?> result = tourService.getPublicTours(filterDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Get tours successfully", result);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get tours fail");
        }
    }

    @GetMapping("/{slug}")
    public ResponseData<?> getTourDetail(@PathVariable String slug) {
        try {
            TourDetailDTO detail = tourService.getTourBySlug(slug);
            return new ResponseData<>(HttpStatus.OK.value(), "Get tour detail successfully", detail);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get tour detail fail");
        }
    }

    @PostMapping("/calculate-price")
    public ResponseData<?> calculatePrice(@RequestBody CalculatePriceRequestDTO requestDTO) {
        try {
            CalculatePriceResponseDTO result = tourService.calculatePrice(requestDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Calculate price successfully", result);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Calculate price fail");
        }
    }

    @GetMapping("/related")
    public ResponseData<?> getRelatedTours(@RequestParam Long tourId,
                                           @RequestParam(defaultValue = "4") int limit) {
        try {
            List<?> result = tourService.getRelatedTours(tourId, limit);
            return new ResponseData<>(HttpStatus.OK.value(), "Get related tours successfully", result);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Get related tours fail");
        }
    }

    @GetMapping("/search")
    public ResponseData<?> searchTours(@RequestParam String keyword,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<?> result = tourService.searchTours(keyword, page, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "Search tours successfully", result);
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Search tours fail");
        }
    }
}