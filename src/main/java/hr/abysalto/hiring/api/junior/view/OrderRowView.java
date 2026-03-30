package hr.abysalto.hiring.api.junior.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRowView {
	private Long orderNr;
	private String buyerName;
	private String status;
	private LocalDateTime orderTime;
	private String paymentMethod;
	private String deliveryAddress;
	private String contactNumber;
	private String note;
	private List<OrderLineView> lineItems;
	private BigDecimal totalAmount;
	private String currency;
}
