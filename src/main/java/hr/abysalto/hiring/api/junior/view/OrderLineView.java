package hr.abysalto.hiring.api.junior.view;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class OrderLineView {
	private final String itemName;
	private final int quantity;
	private final BigDecimal unitPrice;

	public OrderLineView(String itemName, int quantity, BigDecimal unitPrice) {
		this.itemName = itemName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}
}
