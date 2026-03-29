package hr.abysalto.hiring.api.junior.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@GetMapping({ "", "/" })
	public String ordersPage() {
		return "orders/index";
	}

}
