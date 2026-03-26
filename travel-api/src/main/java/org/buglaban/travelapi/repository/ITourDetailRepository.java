package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.TourDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITourDetailRepository extends JpaRepository<TourDetail, Long> {
    List<TourDetail> findByTourIdOrderByDayNumber(Long tourId);
    void deleteByTourId(Long tourId);
}