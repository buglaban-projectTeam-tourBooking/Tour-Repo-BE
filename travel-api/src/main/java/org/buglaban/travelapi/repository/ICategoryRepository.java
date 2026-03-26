package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ICategoryRepository extends JpaRepository <Category, Long> {
    Optional<Category> findById(Long categoryId);
}
