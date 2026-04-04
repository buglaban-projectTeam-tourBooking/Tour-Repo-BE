package org.buglaban.travelapi.service;

import org.buglaban.travelapi.dto.request.order.OrderCreateRequest;
import org.buglaban.travelapi.dto.response.PageResponse;
import org.buglaban.travelapi.dto.response.order.OrderDetailResponse;
import org.buglaban.travelapi.dto.response.order.OrderResponse;

public interface IOrderService {

    /**
     * Tạo đơn hàng mới (US-05)
     */
    Long createOrder(OrderCreateRequest request);

    /**
     * Lấy chi tiết đơn hàng
     */
    OrderDetailResponse getOrderDetail(Long orderId);

    /**
     * Lấy danh sách đơn hàng của khách hàng hiện tại (US-06, US-11)
     */
    PageResponse<OrderResponse> getMyOrders(int page, int size);

    /**
     * Admin lấy tất cả đơn hàng (có thể lọc theo status) (US-10)
     */
    PageResponse<OrderResponse> getAllOrders(int page, int size, String status);

    /**
     * Hủy đơn hàng (US-07)
     */
    void cancelOrder(Long orderId);

    /**
     * Cập nhật trạng thái đơn hàng (Admin)
     */
    void updateOrderStatus(Long orderId, String status);
}