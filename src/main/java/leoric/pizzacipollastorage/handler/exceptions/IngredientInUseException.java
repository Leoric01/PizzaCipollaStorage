package leoric.pizzacipollastorage.handler.exceptions;

public class IngredientInUseException extends RuntimeException {
    public IngredientInUseException(String ingredientId, String ingredientName, String menuItems) {
        super("Cannot delete ingredient '" + ingredientName + "' (" + ingredientId + "), it is used in: " + menuItems);
    }
}