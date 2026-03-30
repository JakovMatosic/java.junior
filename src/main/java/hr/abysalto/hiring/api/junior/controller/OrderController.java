package hr.abysalto.hiring.api.junior.controller;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.manager.OrderManager;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private BuyerAddressRepository buyerAddressRepository;

    @GetMapping({"", "/"})
    public String ordersPage(@RequestParam(value = "sort", required = false) String sort, Model model) {
        List<Order> orders = orderManager.getAllOrders();
        orders = orderManager.sortOrders(orders, sort);

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

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable("id") Long id, Model model) {
        Order order = orderManager.getOrderById(id);
        
        if (order == null) {
            return "redirect:/orders";
        }
        
        model.addAttribute("order", order);
        model.addAttribute("allBuyers", buyerRepository.findAll());
        model.addAttribute("allAddresses", buyerAddressRepository.findAll());
        model.addAttribute("isEdit", true);
        return "order/new_order";
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute("order") Order order) {
        String validationResult = orderManager.validateAndSaveOrder(order);
        
        if (validationResult != null) {
            return validationResult;
        }
        
        return "redirect:/orders";
    }

    @PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute("order") Order order) {
        String validationResult = orderManager.validateAndSaveOrder(order);
        
        if (validationResult != null) {
            return validationResult;
        }
        
        return "redirect:/orders";
    }

    @PostMapping("/updateStatus/{id}")
    public String updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        orderManager.updateOrderStatus(id, status);
        return "redirect:/orders";
    }


    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderManager.deleteOrder(id);
        // Redirects back to the list after deletion
        return "redirect:/orders";
    }
}