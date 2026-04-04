package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String orderCode);

    // Lấy đơn hàng của một user
    Page<Order> findByUserId(Long userId, Pageable pageable);

    // Admin lấy tất cả đơn hàng, có thể lọc theo status
    Page<Order> findByOrderStatus(String orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByUserId(Long userId, Pageable pageable);
}
