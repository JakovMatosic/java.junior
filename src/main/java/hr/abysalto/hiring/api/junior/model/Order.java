package hr.abysalto.hiring.api.junior.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("order") // to avoid SQL reserved "ORDER"
@AccessType(AccessType.Type.PROPERTY)
public class Order {
	@Id
	private Long orderNr;

	private Long buyerId;

	@Transient // populate manually or use Join
	private Buyer buyer;

	@Transient
	private OrderStatus orderStatus;

	@Column("order_status")
	public String getStringOrderStatus() {
		return this.orderStatus != null ? this.orderStatus.toString() : null;
	}

	public void setStringOrderStatus(String orderStatusString) {
		this.orderStatus = OrderStatus.fromString(orderStatusString);
	}

	private LocalDateTime orderTime;

	private List<OrderItem> orderItems;

	@Transient
	private PaymentOption paymentOption;

	@Column("payment_option")
	public String getStringPaymentOption() {
		return this.paymentOption != null ? this.paymentOption.toString() : null;
	}

	public void setStringPaymentOption(String paymentOptionString) {
		this.paymentOption = PaymentOption.fromString(paymentOptionString);
	}

	private Long deliveryAddressId; // DB column delivery_address_id

	@Transient
	private BuyerAddress deliveryAddress; // actual object for the UI

	private String contactNumber;
	private String note;
	private String currency;
	private BigDecimal totalPrice;
}