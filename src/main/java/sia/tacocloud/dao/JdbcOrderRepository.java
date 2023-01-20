package sia.tacocloud.dao;

import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sia.tacocloud.model.Ingredient;
import sia.tacocloud.model.Taco;
import sia.tacocloud.model.TacoOrder;

import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        PreparedStatementCreator statementCreator = getTacoOrderPreparedStatementCreator(tacoOrder);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(statementCreator, keyHolder);
        tacoOrder.setId(keyHolder.getKey().longValue());
        saveTacos(tacoOrder);
        return tacoOrder;
    }

    private void saveTacos(TacoOrder order) {
        List<Taco> tacos = order.getTacos();
        long orderId = order.getId();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }
    }

    private Taco saveTaco(long orderId, int tacoOrderKey, Taco taco) {
        PreparedStatementCreator statementCreator = getTacoPreparedStatementCreator(taco, orderId, tacoOrderKey);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(statementCreator, keyHolder);
        taco.setId(keyHolder.getKey().longValue());
        saveIngredientsRefs(taco);
        return taco;
    }

    private void saveIngredientsRefs(Taco taco) {
        List<Ingredient> ingredients = taco.getIngredients();
        long tacoId = taco.getId();
        int key = 0;
        for (Ingredient ingredient : ingredients) {
            jdbcOperations.update(
                    """
                            INSERT INTO INGREDIENT_REF(INGREDIENT, TACO, TACO_KEY)
                            VALUES (?, ?, ?)
                            """,
                    ingredient.getId(), tacoId, key++
            );
        }

    }

    private PreparedStatementCreator getTacoPreparedStatementCreator(Taco taco, long tacoOrderId, long tacoOrderKey) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory statementFactory = getTacoPreparedStatementCreatorFactory();
        return statementFactory.newPreparedStatementCreator(
                List.of(taco.getName(), taco.getCreatedAt(), tacoOrderId, tacoOrderKey));
    }

    private PreparedStatementCreatorFactory getTacoPreparedStatementCreatorFactory() {
        PreparedStatementCreatorFactory statementFactory = new PreparedStatementCreatorFactory(
                """
                        INSERT INTO TACO(name, created_at, TACO_ORDER, TACO_ORDER_KEY)
                        VALUES (?, ?, ?, ?)
                        """,
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );

        statementFactory.setReturnGeneratedKeys(true);
        return statementFactory;
    }

    private PreparedStatementCreator getTacoOrderPreparedStatementCreator(TacoOrder order) {
        PreparedStatementCreatorFactory statementFactory = getTacoOrderPreparedStatementCreatorFactory();
        order.setCreatedAt(new Date());
        return statementFactory.newPreparedStatementCreator(
                List.of(order.getDeliveryName(),
                        order.getDeliveryStreet(),
                        order.getDeliveryCity(),
                        order.getDeliveryState(),
                        order.getDeliveryZip(),
                        order.getCcNumber(),
                        order.getCcExpiration(),
                        order.getCcCVV(),
                        order.getCreatedAt()));
    }

    private PreparedStatementCreatorFactory getTacoOrderPreparedStatementCreatorFactory() {
        PreparedStatementCreatorFactory statementFactory = new PreparedStatementCreatorFactory(
                """
                        INSERT INTO Taco_Order 
                        (delivery_name, delivery_street, delivery_city,
                        delivery_state, delivery_zip, cc_number,
                        cc_expiration, cc_cvv, placed_at)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);

        statementFactory.setReturnGeneratedKeys(true);
        return statementFactory;

    }
}
