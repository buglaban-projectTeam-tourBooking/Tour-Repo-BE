package org.buglaban.travelapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.buglaban.travelapi.dto.request.order.OrderCreateRequest;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.order.OrderDetailResponse;
import org.buglaban.travelapi.dto.response.order.OrderItemResponse;
import org.buglaban.travelapi.dto.response.order.OrderResponse;
import org.buglaban.travelapi.exception.DataNotFoundException;
import org.buglaban.travelapi.model.*;
import org.buglaban.travelapi.repository.*;
import org.buglaban.travelapi.service.IOrderService;
import org.buglaban.travelapi.util.OrderStatus;
import org.buglaban.travelapi.util.PaymentStatus;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ITourRepository tourRepository;
    private final ITourScheduleRepository tourScheduleRepository;
    private final IUserRepository userRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderParticipantRepository orderParticipantRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public Long createOrder(OrderCreateRequest request) {
        Long userId = 4L;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng"));

        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tour"));

        TourSchedule schedule = tourScheduleRepository.findById(request.getTourScheduleId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy lịch tour"));

        // Kiểm tra chỗ ngồi
        int totalParticipants = request.getAdultQuantity() + request.getChildQuantity();
        if (schedule.getAvailableSeats() < totalParticipants) {
            throw new RuntimeException("Không đủ chỗ trống cho tour này");
        }

        // Tính tổng tiền
        BigDecimal totalAmount = calculateTotalAmount(tour, request);

        // Tạo Order
        Order order = Order.builder()
                .user(user)
                .orderCode("ORDER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .totalAmount(totalAmount)
                .finalAmount(totalAmount)
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .customerNote(request.getCustomerNote())
                .paymentDate(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Tạo OrderDetail
        OrderDetail orderDetail = OrderDetail.builder()
                .order(savedOrder)
                .tour(tour)
                .tourSchedule(schedule)
                .tourName(tour.getTourName())
                .departureDate(schedule.getDepartureDate())
                .returnDate(schedule.getReturnDate())
                .adultQuantity(request.getAdultQuantity())
                .childQuantity(request.getChildQuantity())
                .infantQuantity(request.getInfantQuantity())
                .adultPrice(tour.getAdultPrice())
                .childPrice(tour.getChildPrice())
                .infantPrice(tour.getInfantPrice() != null ? tour.getInfantPrice() : BigDecimal.ZERO)
                .build();

        orderDetail.calculateSubtotal();
        OrderDetail savedDetail = orderDetailRepository.save(orderDetail);

        // Tạo danh sách hành khách
        if (request.getParticipants() != null) {
            request.getParticipants().forEach(p -> {
                OrderParticipant participant = mapper.map(p, OrderParticipant.class);
                participant.setOrderDetail(savedDetail);
                orderParticipantRepository.save(participant);
            });
        }

        // Cập nhật số chỗ còn lại
        schedule.setAvailableSeats(schedule.getAvailableSeats() - totalParticipants);
        tourScheduleRepository.save(schedule);

        log.info("Đơn hàng {} đã được tạo thành công cho user {}", savedOrder.getOrderCode(), user.getFullName());

        return savedOrder.getId();
    }

    private BigDecimal calculateTotalAmount(Tour tour, OrderCreateRequest request) {
        BigDecimal adultTotal = tour.getAdultPrice()
                .multiply(BigDecimal.valueOf(request.getAdultQuantity()));

        BigDecimal childTotal = (tour.getChildPrice() != null)
                ? tour.getChildPrice().multiply(BigDecimal.valueOf(request.getChildQuantity()))
                : BigDecimal.ZERO;

        return adultTotal.add(childTotal);
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        return mapper.map(order, OrderDetailResponse.class);
    }

    @Override
    public PageResponse<OrderResponse> getMyOrders(int page, int size) {
        Long userId = 4L; // TODO: Sau này lấy từ JWT

        Page<Order> orderPage = orderRepository.findByUserId(userId, PageRequest.of(page, size));

        List<OrderResponse> responses = orderPage.getContent().stream()
                .map(order -> {
                    OrderResponse res = mapper.map(order, OrderResponse.class);
                    String tourName = order.getOrderDetails().stream()
                            .findFirst()                          // Lấy detail đầu tiên
                            .map(detail -> detail.getTour())      // Từ detail lấy ra Tour
                            .map(tour -> tour.getTourName())      // Từ tour lấy ra Name
                            .orElse("N/A");
                    res.setTourName(tourName);
                    return res;
                })
                .collect(Collectors.toList());

        return PageResponse.<OrderResponse>builder()
                .page(page)
                .pageSize(size)
                .totalPage(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())   // Thêm dòng này
                .items(responses)
                .build();
    }

    @Override
    public PageResponse<OrderResponse> getAllOrders(int page, int size, String status) {
        Page<Order> orderPage;

        if (status != null && !status.trim().isEmpty()) {
            orderPage = orderRepository.findByOrderStatus(status, PageRequest.of(page, size));
        } else {
            orderPage = orderRepository.findAll(PageRequest.of(page, size));
        }

        List<OrderResponse> responses = orderPage.getContent().stream()
                .map(order -> {
                    OrderResponse res = mapper.map(order, OrderResponse.class);
                    String tourName = order.getOrderDetails().stream()
                            .findFirst()                          // Lấy detail đầu tiên
                            .map(detail -> detail.getTour())      // Từ detail lấy ra Tour
                            .map(tour -> tour.getTourName())      // Từ tour lấy ra Name
                            .orElse("N/A");
                    res.setTourName(tourName);
                    return res;
                })
                .collect(Collectors.toList());

        return PageResponse.<OrderResponse>builder()
                .page(page)
                .pageSize(size)
                .totalPage(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .items(responses)                    // Đổi thành .items()
                .build();
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Đơn hàng đã bị hủy trước đó");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING && order.getOrderStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Không thể hủy đơn hàng ở trạng thái hiện tại");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Đơn hàng {} đã bị hủy bởi người dùng", order.getOrderCode());
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        log.info("Đơn hàng {} đã được cập nhật trạng thái thành {}", order.getOrderCode(), newStatus);
    }
}