package leoric.pizzacipollastorage.handler.exceptions;

public class DuplicateIngredientNameException extends RuntimeException {
    public DuplicateIngredientNameException(String message) {
        super(message);
    }
}