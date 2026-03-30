package hr.abysalto.hiring.api.junior.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import hr.abysalto.hiring.api.junior.view.OrderLineView;
import hr.abysalto.hiring.api.junior.view.OrderRowView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private static List<OrderRowView> mockOrders() {
		List<OrderRowView> list = new ArrayList<>();
		list.add(new OrderRowView(
				1L,
				"Han Solo",
				"Pending",
				LocalDateTime.of(2026, 3, 28, 14, 30),
				"Cash",
				"123 Docking Bay, Mos Eisley",
				"+385 99 111 2222",
				"Leave at the door",
				List.of(
						new OrderLineView("Hyperdrive coil", 1, new BigDecimal("2499.99")),
						new OrderLineView("Blaster power cell", 4, new BigDecimal("12.50"))),
				new BigDecimal("2549.99"),
				"EUR"));
		list.add(new OrderRowView(
				2L,
				"Leia Organa",
				"In preparation",
				LocalDateTime.of(2026, 3, 29, 9, 15),
				"Card (upfront)",
				"Royal Plaza 1, Alderaan Memorial",
				"+385 99 333 4444",
				"",
				List.of(
						new OrderLineView("Data tape", 3, new BigDecimal("5.00")),
						new OrderLineView("Comlink", 1, new BigDecimal("89.00"))),
				new BigDecimal("104.00"),
				"USD"));
		list.add(new OrderRowView(
				3L,
				"Jabba Hutt",
				"Completed",
				LocalDateTime.of(2026, 3, 20, 18, 0),
				"Card (on delivery)",
				"Palace Road 7, Tatooine",
				"+385 91 000 0000",
				"Heavy shipment — use rear entrance",
				List.of(new OrderLineView("Frozen carbonite crate", 1, new BigDecimal("15000.00"))),
				new BigDecimal("15000.00"),
				"EUR"));
		return list;
	}

	@GetMapping({ "", "/" })
	public String ordersPage(@RequestParam(required = false) String sort, Model model) {
		List<OrderRowView> orders = new ArrayList<>(mockOrders());
		if ("alphabet".equals(sort)) {
			orders.sort(Comparator.comparing(OrderRowView::getBuyerName, String.CASE_INSENSITIVE_ORDER));
		} else if ("price".equals(sort)) {
			orders.sort(Comparator.comparing(OrderRowView::getTotalAmount));
		}
		model.addAttribute("orderList", orders);
		model.addAttribute("sort", sort != null ? sort : "");
		return "orders/index";
	}

	@GetMapping("/addnew")
	public String addNewOrder() {
		return "orders/neworder";
	}

	@GetMapping("/showFormForUpdate/{id}")
	public String updateForm(@PathVariable("id") long id, Model model) {
		model.addAttribute("orderId", id);
		return "orders/updateorder";
	}

	@GetMapping("/deleteOrder/{id}")
	public String deleteOrder(@PathVariable("id") long id) {
		return "redirect:/orders";
	}

}
