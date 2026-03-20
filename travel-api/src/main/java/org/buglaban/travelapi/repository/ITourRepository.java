package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.Tour;
import org.buglaban.travelapi.util.TourStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ITourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {

    Optional<Tour> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByTourCode(String tourCode);

    @Modifying
    @Transactional
    @Query("UPDATE Tour t SET t.viewCount = t.viewCount + 1 WHERE t.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query("SELECT t FROM Tour t JOIN FETCH t.category WHERE t.status = :status ORDER BY t.createdAt DESC")
    List<Tour> findRelatedTours(@Param("status") TourStatus status);
}
