package sia.tacocloud.dao;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.model.TacoOrder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<TacoOrder, UUID> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

}
