package hr.abysalto.hiring.api.junior.repository;

import hr.abysalto.hiring.api.junior.model.Order;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query("SELECT * FROM \"order\"") // Quotes ensure the keyword doesn't break the query
    List<Order> findAllOrders();
}