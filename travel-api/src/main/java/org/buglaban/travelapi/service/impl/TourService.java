package org.buglaban.travelapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.buglaban.travelapi.dto.request.tour.AdminTourCreateUpdateDTO;
import org.buglaban.travelapi.dto.request.tour.CalculatePriceRequestDTO;
import org.buglaban.travelapi.dto.request.tour.TourFilterRequestDTO;
import org.buglaban.travelapi.dto.request.tour.TourScheduleRequestDTO;
import org.buglaban.travelapi.dto.response.tour.*;
import org.buglaban.travelapi.service.ITourService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.buglaban.travelapi.dto.request.*;
import org.buglaban.travelapi.dto.response.*;
import org.buglaban.travelapi.exception.DataNotFoundException;
import org.buglaban.travelapi.model.*;
import org.buglaban.travelapi.repository.*;
import org.buglaban.travelapi.util.specification.TourSpecification;
import org.buglaban.travelapi.util.ScheduleStatus;
import org.buglaban.travelapi.util.TourStatus;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TourService implements ITourService {
    private final ITourRepository tourRepository;
    private final ITourScheduleRepository scheduleRepository;
    private final ITourDetailRepository detailRepository;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;
    private final IReviewRepository reviewRepository;
    private final ModelMapper mapper;


    @Override
    public Page<TourSummaryDTO> getPublicTours(TourFilterRequestDTO filterDTO) {
        Specification<Tour> spec = TourSpecification.filter(filterDTO);
        Sort sort = buildSort(filterDTO.getSort());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getPageSize(), sort);
        Page<Tour> page = tourRepository.findAll(spec, pageable);
        return page.map(this::toSummaryDTO);
    }

    @Override
    public TourDetailDTO getTourBySlug(String slug) {
        Tour tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new DataNotFoundException("Tour not found: " + slug));
        tourRepository.incrementViewCount(tour.getId());
        return toDetailDTO(tour);
    }

    @Override
    public CalculatePriceResponseDTO calculatePrice(CalculatePriceRequestDTO req) {
        Tour tour;
        if (req.getTourId() != null) {
            tour = tourRepository.findById(req.getTourId()).get();
        } else {
            tour = tourRepository.findBySlug(req.getSlug())
                    .orElseThrow(() -> new DataNotFoundException("Tour not found"));
        }

        TourSchedule schedule = scheduleRepository.findById(req.getScheduleId())
                .orElseThrow(() -> new DataNotFoundException("Schedule not found"));

        int availableSlots = schedule.getAvailableSeats() - schedule.getBookedSeats();
        int totalPax = req.getAdultQty() + req.getChildQty();
        boolean isAvailable = availableSlots >= totalPax;

        BigDecimal adultSub = tour.getAdultPrice().multiply(BigDecimal.valueOf(req.getAdultQty()));
        BigDecimal childSub = tour.getChildPrice() != null
                ? tour.getChildPrice().multiply(BigDecimal.valueOf(req.getChildQty()))
                : BigDecimal.ZERO;
        BigDecimal infantSub = tour.getInfantPrice() != null
                ? tour.getInfantPrice().multiply(BigDecimal.valueOf(req.getInfantQty()))
                : BigDecimal.ZERO;
        BigDecimal subtotal = adultSub.add(childSub).add(infantSub);

        return CalculatePriceResponseDTO.builder()
                .adultSubtotal(adultSub)
                .childSubtotal(childSub)
                .infantSubtotal(infantSub)
                .subtotal(subtotal)
                .finalAmount(subtotal)
                .availableSlots(availableSlots)
                .isAvailable(isAvailable)
                .build();
    }

    @Override
    public List<TourSummaryDTO> getRelatedTours(Long tourId, int limit) {
        Tour current = tourRepository.findById(tourId).get();
        return tourRepository.findRelatedTours(TourStatus.PUBLISHED)
                .stream()
                .filter(t -> !t.getId().equals(tourId))
                .filter(t -> current.getCategory() != null
                        && t.getCategory() != null
                        && t.getCategory().getId().equals(current.getCategory().getId()))
                .limit(limit)
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TourSummaryDTO> searchTours(String keyword, int page, int pageSize) {
        Specification<Tour> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), TourStatus.PUBLISHED),
                cb.or(
                        cb.like(cb.lower(root.get("tourName")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("destination")), "%" + keyword.toLowerCase() + "%")
                )
        );
        Pageable pageable = PageRequest.of(page, pageSize);
        return tourRepository.findAll(spec, pageable).map(this::toSummaryDTO);
    }


    @Override
    public Page<AdminTourSummaryDTO> getAdminTours(TourFilterRequestDTO filterDTO) {
        Specification<Tour> spec = TourSpecification.filter(filterDTO);
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return tourRepository.findAll(spec, pageable).map(this::toAdminSummaryDTO);
    }

    private String generateSlug(String tourName) {
        return tourName.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }

    @Override
    public Long createTour(AdminTourCreateUpdateDTO dto) {
        if (dto.getSlug() == null || dto.getSlug().trim().isEmpty()) {
            dto.setSlug(generateSlug(dto.getTourName()));
        }
        Tour tour = Tour.builder()
                .tourName(dto.getTourName())
                .tourCode(dto.getTourCode())
                .slug(dto.getSlug())
                .shortDescription(dto.getShortDescription())
                .description(dto.getDescription())
                .featuredImage(dto.getFeaturedImage())
                .videoUrl(dto.getVideoUrl())
                .departureLocation(dto.getDepartureLocation())
                .destination(dto.getDestination())
                .durationDays(dto.getDurationDays())
                .durationNights(dto.getDurationNights())
                .basePrice(dto.getBasePrice())
                .adultPrice(dto.getAdultPrice())
                .childPrice(dto.getChildPrice())
                .infantPrice(dto.getInfantPrice())
                .status(dto.getStatus() != null ? dto.getStatus() : TourStatus.DRAFT)
                .viewCount(0)
                .build();

        if (dto.getCategoryId() != null) {
            tour.setCategory(categoryRepository.findById(dto.getCategoryId()).get());
        }

        tourRepository.save(tour);

        if (dto.getItinerary() != null) {
            List<TourDetail> details = dto.getItinerary().stream().map(d ->
                    TourDetail.builder()
                            .tour(tour)
                            .dayNumber(d.getDayNumber())
                            .dayTitle(d.getDayTitle())
                            .dep(d.getDep())
                            .description(d.getDescription())
                            .image(d.getImage())
                            .build()
            ).collect(Collectors.toList());
            detailRepository.saveAll(details);
        }

        if (dto.getSchedules() != null) {
            List<TourSchedule> schedules = dto.getSchedules().stream().map(s ->
                    TourSchedule.builder()
                            .tour(tour)
                            .departureDate(LocalDate.parse(s.getDepartureDate()))
                            .returnDate(LocalDate.parse(s.getReturnDate()))
                            .availableSeats(s.getAvailableSeats())
                            .bookedSeats(0)
                            .note(s.getNote())
                            .status(s.getStatus() != null ? ScheduleStatus.valueOf(s.getStatus()) : ScheduleStatus.AVAILABLE)
                            .build()
            ).collect(Collectors.toList());
            scheduleRepository.saveAll(schedules);
        }

        return tour.getId();
    }

    @Override
    public void updateTour(Long id, AdminTourCreateUpdateDTO dto) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Tour not found: " + id));

        tour.setTourName(dto.getTourName());
        tour.setTourCode(dto.getTourCode());
        tour.setSlug(dto.getSlug());
        tour.setShortDescription(dto.getShortDescription());
        tour.setDescription(dto.getDescription());
        tour.setFeaturedImage(dto.getFeaturedImage());
        tour.setVideoUrl(dto.getVideoUrl());
        tour.setDepartureLocation(dto.getDepartureLocation());
        tour.setDestination(dto.getDestination());
        tour.setDurationDays(dto.getDurationDays());
        tour.setDurationNights(dto.getDurationNights());
        tour.setBasePrice(dto.getBasePrice());
        tour.setAdultPrice(dto.getAdultPrice());
        tour.setChildPrice(dto.getChildPrice());
        tour.setInfantPrice(dto.getInfantPrice());
        tour.setStatus(dto.getStatus());

        if (dto.getCategoryId() != null) {
            Optional <Category> category = categoryRepository.findById(dto.getCategoryId());
            if (category.isPresent()) {
                tour.setCategory(category.get());
            } else {
                throw new DataNotFoundException("Category not found: " + dto.getCategoryId());
            }
        }

        // Replace itinerary
        if (dto.getItinerary() != null) {
            detailRepository.deleteByTourId(id);
            List<TourDetail> details = dto.getItinerary().stream().map(d ->
                    TourDetail.builder()
                            .tour(tour)
                            .dayNumber(d.getDayNumber())
                            .dayTitle(d.getDayTitle())
                            .dep(d.getDep())
                            .description(d.getDescription())
                            .image(d.getImage())
                            .build()
            ).collect(Collectors.toList());
            detailRepository.saveAll(details);
        }

        tourRepository.save(tour);
    }

    @Override
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new DataNotFoundException("Tour not found: " + id);
        }
        tourRepository.deleteById(id);
    }

    @Override
    public void deleteTours(List<Long> ids) {
        tourRepository.deleteAllById(ids);
    }

    @Override
    public void changeToursStatus(List<Long> ids, String status) {
        TourStatus tourStatus = TourStatus.valueOf(status);
        List<Tour> tours = tourRepository.findAllById(ids);
        tours.forEach(t -> t.setStatus(tourStatus));
        tourRepository.saveAll(tours);
    }

    @Override
    public TourDetailDTO previewTour(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Tour not found: " + id));
        return toDetailDTO(tour);
    }

    @Override
    public Long addSchedule(Long tourId, TourScheduleRequestDTO dto) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new DataNotFoundException("Tour not found: " + tourId));
        TourSchedule schedule = TourSchedule.builder()
                .tour(tour)
                .departureDate(LocalDate.parse(dto.getDepartureDate()))
                .returnDate(LocalDate.parse(dto.getReturnDate()))
                .availableSeats(dto.getAvailableSeats())
                .bookedSeats(0)
                .note(dto.getNote())
                .status(dto.getStatus() != null ? ScheduleStatus.valueOf(dto.getStatus()) : ScheduleStatus.AVAILABLE)
                .build();
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @Override
    public List<TourScheduleDTO> getSchedulesByTour(Long tourId) {
        return scheduleRepository.findByTourId(tourId)
                .stream().map(this::toScheduleDTO).collect(Collectors.toList());
    }


    private TourSummaryDTO toSummaryDTO(Tour tour) {
        return TourSummaryDTO.builder()
                .id(tour.getId())
                .tourName(tour.getTourName())
                .slug(tour.getSlug())
                .tourCode(tour.getTourCode())
                .featuredImage(tour.getFeaturedImage())
                .destination(tour.getDestination())
                .departureLocation(tour.getDepartureLocation())
                .durationDays(tour.getDurationDays())
                .durationNights(tour.getDurationNights())
                .adultPrice(tour.getAdultPrice())
                .priceFrom(tour.getAdultPrice())
                .status(tour.getStatus())
                .categoryName(tour.getCategory() != null ? tour.getCategory().getCategoryName() : null)
                .averageRating(0.0)
                .totalReviews(0L)
                .build();
    }

    private TourDetailDTO toDetailDTO(Tour tour) {
        List<TourDayDTO> itinerary = detailRepository.findByTourIdOrderByDayNumber(tour.getId())
                .stream().map(d -> TourDayDTO.builder()
                        .id(d.getId())
                        .dayNumber(d.getDayNumber())
                        .dayTitle(d.getDayTitle())
                        .dep(d.getTour().getDepartureLocation())
                        .description(d.getDescription())
                        .image(d.getImage())
                        .build())
                .collect(Collectors.toList());

        List<TourScheduleDTO> schedules = scheduleRepository.findByTourId(tour.getId())
                .stream().map(this::toScheduleDTO).collect(Collectors.toList());

        return TourDetailDTO.builder()
                .id(tour.getId())
                .tourName(tour.getTourName())
                .slug(tour.getSlug())
                .tourCode(tour.getTourCode())
                .shortDescription(tour.getShortDescription())
                .description(tour.getDescription())
                .featuredImage(tour.getFeaturedImage())
                .videoUrl(tour.getVideoUrl())
                .destination(tour.getDestination())
                .departureLocation(tour.getDepartureLocation())
                .durationDays(tour.getDurationDays())
                .durationNights(tour.getDurationNights())
                .adultPrice(tour.getAdultPrice())
                .childPrice(tour.getChildPrice())
                .infantPrice(tour.getInfantPrice())
                .status(tour.getStatus())
                .viewCount(tour.getViewCount())
                .categoryName(tour.getCategory() != null ? tour.getCategory().getCategoryName() : null)
                .categorySlug(tour.getCategory() != null ? tour.getCategory().getSlug() : null)
                .itinerary(itinerary)
                .schedules(schedules)
                .averageRating(0.0)
                .totalReviews(0L)
                .build();
    }

    private TourScheduleDTO toScheduleDTO(TourSchedule s) {
        return TourScheduleDTO.builder()
                .id(s.getId())
                .departureDate(s.getDepartureDate())
                .returnDate(s.getReturnDate())
                .availableSeats(s.getAvailableSeats())
                .bookedSeats(s.getBookedSeats())
                .availableSlots(s.getAvailableSeats() - s.getBookedSeats())
                .status(s.getStatus())
                .note(s.getNote())
                .build();
    }

    private AdminTourSummaryDTO toAdminSummaryDTO(Tour tour) {
        Long scheduleCount = scheduleRepository.countByTourIdAndStatus(tour.getId(), ScheduleStatus.AVAILABLE);
        return AdminTourSummaryDTO.builder()
                .id(tour.getId())
                .tourName(tour.getTourName())
                .tourCode(tour.getTourCode())
                .slug(tour.getSlug())
                .status(tour.getStatus())
                .adultPrice(tour.getAdultPrice())
                .destination(tour.getDestination())
                .categoryName(tour.getCategory() != null ? tour.getCategory().getCategoryName() : null)
                .createdByName(tour.getCreatedBy() != null ? tour.getCreatedBy().getFullName() : null)
                .totalBookings(0L)
                .availableSchedulesCount(scheduleCount)
                .build();
    }

    private Sort buildSort(String sort) {
        if (sort == null) return Sort.by(Sort.Direction.DESC, "createdAt");

        return switch (sort) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "adultPrice");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "adultPrice");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
}
