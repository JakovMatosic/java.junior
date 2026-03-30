package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;
import java.util.List;

public interface OrderManager {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    String validateAndSaveOrder(Order order);
    void deleteOrder(Long id);
    List<Order> sortOrders(List<Order> orders, String sortBy);
    void updateOrderStatus(Long id, String status);
}
