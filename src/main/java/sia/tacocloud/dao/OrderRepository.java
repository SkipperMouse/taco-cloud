package sia.tacocloud.dao;

import sia.tacocloud.model.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
