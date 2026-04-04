package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.OrderDetail;
import org.buglaban.travelapi.model.OrderParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderParticipantRepository extends JpaRepository<OrderParticipant, Long> {

    List<OrderParticipant> findByOrderDetailId(Integer orderDetailId);

    long countByOrderDetailIdAndParticipantType(Integer orderDetailId, String participantType);
}
