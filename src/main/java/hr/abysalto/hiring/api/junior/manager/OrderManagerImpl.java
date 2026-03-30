package hr.abysalto.hiring.api.junior.manager;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderManagerImpl implements OrderManager {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Autowired
    private BuyerAddressRepository buyerAddressRepository;
    
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAllOrders();
        
        for (Order order : orders) {
            buyerRepository.findById(order.getBuyerId())
                    .ifPresent(order::setBuyer);
            
            buyerAddressRepository.findById(order.getDeliveryAddressId())
                    .ifPresent(order::setDeliveryAddress);
        }
        
        return orders;
    }
    
    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    @Override
    public String validateAndSaveOrder(Order order) {
        if (order.getBuyerId() == null || order.getBuyerId() <= 0) {
            return "redirect:/orders/addnew?error=buyer";
        }
        
        if (order.getDeliveryAddressId() == null || order.getDeliveryAddressId() <= 0) {
            return "redirect:/orders/addnew?error=address";
        }
        
        if (order.getOrderStatus() == null) {
            return "redirect:/orders/addnew?error=status";
        }
        
        if (order.getPaymentOption() == null) {
            return "redirect:/orders/addnew?error=payment";
        }
        
        if (order.getCurrency() == null || order.getCurrency().trim().isEmpty()) {
            return "redirect:/orders/addnew?error=currency";
        }
        
        order.setOrderTime(LocalDateTime.now());
        
        // Filter out items with empty or null names and quantities
        if (order.getOrderItems() != null) {
            order.setOrderItems(
                order.getOrderItems().stream()
                    .filter(item -> item.getName() != null && !item.getName().trim().isEmpty())
                    .filter(item -> item.getQuantity() != null && item.getQuantity() > 0)
                    .collect(Collectors.toList())
            );
        }
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return "redirect:/orders/addnew?error=items";
        }
        
        // Calculate total price
        calculateTotalPrice(order);
        
        orderRepository.save(order);
        
        return null; // Return null to indicate success
    }
    
    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    
    @Override
    public List<Order> sortOrders(List<Order> orders, String sortBy) {
        if ("alphabet".equals(sortBy)) {
            orders.sort(Comparator.comparing((Order o) ->
                            o.getBuyer() != null ? o.getBuyer().getLastName() : "", String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(o -> o.getBuyer() != null ? o.getBuyer().getFirstName() : "", String.CASE_INSENSITIVE_ORDER));
        } else if ("price".equals(sortBy)) {
            orders.sort(Comparator.comparing(Order::getTotalPrice, Comparator.nullsLast(Comparator.naturalOrder())));
        }
        return orders;
    }
    
    private void calculateTotalPrice(Order order) {
        BigDecimal sum = BigDecimal.ZERO;
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getPrice() != null && item.getQuantity() != null) {
                    BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                    sum = sum.add(itemTotal);
                }
            }
        }
        order.setTotalPrice(sum);
    }
}
