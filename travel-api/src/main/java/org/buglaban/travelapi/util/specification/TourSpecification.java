package org.buglaban.travelapi.util.specification;
import jakarta.persistence.criteria.*;
import org.buglaban.travelapi.dto.request.tour.TourFilterRequestDTO;
import org.buglaban.travelapi.model.Tour;
import org.buglaban.travelapi.model.TourSchedule;
import org.buglaban.travelapi.util.TourStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
public class TourSpecification {
    public static Specification<Tour> filter(TourFilterRequestDTO req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.getDeparture() != null && !req.getDeparture().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("departureLocation")),
                        "%" + req.getDeparture().toLowerCase() + "%"));
            }
            if (req.getDestination() != null && !req.getDestination().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("destination")),
                        "%" + req.getDestination().toLowerCase() + "%"));
            }
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("adultPrice"), req.getMinPrice()));
            }
            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("adultPrice"), req.getMaxPrice()));
            }
            if (req.getCategorySlug() != null && !req.getCategorySlug().isBlank()) {
                predicates.add(cb.equal(root.get("category").get("slug"), req.getCategorySlug()));
            }
            if (req.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), req.getStatus()));
            } else {
                predicates.add(cb.equal(root.get("status"), TourStatus.PUBLISHED));
            }

            // Filter theo ngày khởi hành từ schedule
            if (req.getDateFrom() != null || req.getDateTo() != null) {
                Subquery<Long> sub = query.subquery(Long.class);
                Root<TourSchedule> scheduleRoot = sub.from(TourSchedule.class);
                sub.select(scheduleRoot.get("tour").get("id"));
                List<Predicate> schedulePreds = new ArrayList<>();
                schedulePreds.add(cb.equal(scheduleRoot.get("tour").get("id"), root.get("id")));
                if (req.getDateFrom() != null)
                    schedulePreds.add(cb.greaterThanOrEqualTo(scheduleRoot.get("departureDate"), req.getDateFrom()));
                if (req.getDateTo() != null)
                    schedulePreds.add(cb.lessThanOrEqualTo(scheduleRoot.get("departureDate"), req.getDateTo()));
                sub.where(schedulePreds.toArray(new Predicate[0]));
                predicates.add(cb.exists(sub));
            }

            // Filter theo số chỗ còn (adultCount)
            if (req.getAdultCount() != null && req.getAdultCount() > 0) {
                Subquery<Long> sub = query.subquery(Long.class);
                Root<TourSchedule> scheduleRoot = sub.from(TourSchedule.class);
                sub.select(scheduleRoot.get("tour").get("id"));
                sub.where(
                        cb.equal(scheduleRoot.get("tour").get("id"), root.get("id")),
                        cb.greaterThanOrEqualTo(
                                cb.diff(scheduleRoot.get("availableSeats"), scheduleRoot.get("bookedSeats")),
                                req.getAdultCount()
                        )
                );
                predicates.add(cb.exists(sub));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
