package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.TourSchedule;
import org.buglaban.travelapi.util.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ITourScheduleRepository extends JpaRepository <TourSchedule, Long> {
    Long countByTourIdAndStatus(Long id, ScheduleStatus scheduleStatus);

    List<TourSchedule> findByTourId(Long tourId);
}
