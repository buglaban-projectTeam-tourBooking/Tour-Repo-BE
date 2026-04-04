package org.buglaban.travelapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.buglaban.travelapi.dto.request.order.OrderCreateRequest;
import org.buglaban.travelapi.dto.request.order.OrderUpdateStatusRequest;
import org.buglaban.travelapi.dto.response.ResponseData;
import org.buglaban.travelapi.dto.response.ResponseFailure;
import org.buglaban.travelapi.dto.response.order.OrderDetailResponse;
import org.buglaban.travelapi.dto.response.order.OrderResponse;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.service.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order/")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final IOrderService orderService;

    /**
     * US-05: Thanh toán & Đặt cọc
     * Tạo đơn hàng mới
     */
    @PostMapping
    public ResponseData<Long> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        try {
            Long orderId = orderService.createOrder(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo đơn hàng thành công", orderId);
        } catch (Exception e) {
            log.error("Create order error: {}", e.getMessage(), e);
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Tạo đơn hàng thất bại: " + e.getMessage());
        }
    }

    /**
     * Lấy chi tiết một đơn hàng (dùng cho cả Customer và Admin)
     */
    @GetMapping("/{id}")
    public ResponseData<OrderDetailResponse> getOrderDetail(@PathVariable Long id) {
        try {
            OrderDetailResponse response = orderService.getOrderDetail(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy chi tiết đơn hàng thành công", response);
        } catch (Exception e) {
            log.error("Get order detail error: {}", e.getMessage(), e);
            return new ResponseFailure(HttpStatus.NOT_FOUND.value(), "Không tìm thấy đơn hàng");
        }
    }

    /**
     * US-06 & US-11: Quản lý đơn hàng của Customer
     * Xem danh sách đơn hàng của chính mình
     */
    @GetMapping("/my-orders")
    public ResponseData<PageResponse<OrderResponse>> getMyOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            PageResponse<OrderResponse> response = orderService.getMyOrders(page, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách đơn hàng thành công", response);
        } catch (Exception e) {
            log.error("Get my orders error: {}", e.getMessage(), e);
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Lấy danh sách đơn hàng thất bại");
        }
    }

    /**
     * US-10: Admin - Quản lý Đơn hàng
     * Admin xem tất cả đơn hàng (có thể lọc theo status)
     */
    @GetMapping("/admin")
    public ResponseData<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "status", required = false) String status) {
        try {
            PageResponse<OrderResponse> response = orderService.getAllOrders(page, pageSize, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách đơn hàng admin thành công", response);
        } catch (Exception e) {
            log.error("Get all orders error: {}", e.getMessage(), e);
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Lấy danh sách đơn hàng thất bại");
        }
    }

    /**
     * US-07: Hủy đơn & Tính phí
     * Customer hủy đơn hàng
     */
    @PutMapping("/{id}/cancel")
    public ResponseData<Void> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Hủy đơn hàng thành công");
        } catch (Exception e) {
            log.error("Cancel order error: {}", e.getMessage(), e);
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Hủy đơn hàng thất bại: " + e.getMessage());
        }
    }

    /**
     * Admin cập nhật trạng thái đơn hàng
     */
    @PutMapping("/{id}/status")
    public ResponseData<Void> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderUpdateStatusRequest request) {
        try {
            orderService.updateOrderStatus(id, request.getStatus());
            return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật trạng thái đơn hàng thành công");
        } catch (Exception e) {
            log.error("Update order status error: {}", e.getMessage(), e);
            return new ResponseFailure(HttpStatus.BAD_REQUEST.value(), "Cập nhật trạng thái thất bại: " + e.getMessage());
        }
    }
}
