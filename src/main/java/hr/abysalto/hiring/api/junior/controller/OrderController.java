package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private BuyerAddressRepository buyerAddressRepository;

    @GetMapping({"", "/"})
    public String ordersPage(@RequestParam(value = "sort", required = false) String sort, Model model) {
        List<Order> orders = orderRepository.findAllOrders();

        for (Order order : orders) {
            buyerRepository.findById(order.getBuyerId())
                    .ifPresent(order::setBuyer);

            buyerAddressRepository.findById(order.getDeliveryAddressId())
                    .ifPresent(order::setDeliveryAddress);
        }

        if ("alphabet".equals(sort)) {
            orders.sort(Comparator.comparing((Order o) ->
                            o.getBuyer() != null ? o.getBuyer().getLastName() : "", String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(o -> o.getBuyer() != null ? o.getBuyer().getFirstName() : "", String.CASE_INSENSITIVE_ORDER));
        } else if ("price".equals(sort)) {
            orders.sort(Comparator.comparing(Order::getTotalPrice, Comparator.nullsLast(Comparator.naturalOrder())));
        }

        model.addAttribute("orderList", orders);
        model.addAttribute("sort", sort);

        return "order/index";
    }

    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderRepository.deleteById(id);
        // Redirects back to the list after deletion
        return "redirect:/orders";
    }
}