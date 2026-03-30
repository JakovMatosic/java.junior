package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/addnew")
    public String showNewOrderForm(Model model) {
        Order order = new Order();
        // Initialize with one empty item so the user has a row to fill
        order.setOrderItems(new java.util.ArrayList<>());
        order.getOrderItems().add(new OrderItem());

        // We need to pass the list of existing buyers and addresses for the dropdowns
        model.addAttribute("order", order);
        model.addAttribute("allBuyers", buyerRepository.findAll());
        model.addAttribute("allAddresses", buyerAddressRepository.findAll());
        return "order/new_order"; // Adjust path if needed
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute("order") Order order) {
        order.setOrderTime(LocalDateTime.now());
        orderRepository.save(order);
        return "redirect:/orders";
    }


    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderRepository.deleteById(id);
        // Redirects back to the list after deletion
        return "redirect:/orders";
    }
}