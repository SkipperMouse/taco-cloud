package sia.tacocloud.utils;

import sia.tacocloud.model.Ingredient;
import sia.tacocloud.model.IngredientUDT;
import sia.tacocloud.model.Taco;
import sia.tacocloud.model.TacoUDT;

public class TacoUDTUtils {
    public static IngredientUDT toIngredientUDT(Ingredient ingredient) {
        return new IngredientUDT(ingredient.getName(), ingredient.getType());
    }

    public static TacoUDT toTacoUdt(Taco taco) {
        return new TacoUDT(taco.getName(), taco.getIngredients());
    }
}
