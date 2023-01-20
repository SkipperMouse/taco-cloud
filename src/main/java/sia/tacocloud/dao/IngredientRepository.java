package sia.tacocloud.dao;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.model.Ingredient;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
