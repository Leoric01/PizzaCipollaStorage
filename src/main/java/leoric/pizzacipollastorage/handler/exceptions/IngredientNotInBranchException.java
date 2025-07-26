package leoric.pizzacipollastorage.handler.exceptions;

public class IngredientNotInBranchException extends RuntimeException {
    public IngredientNotInBranchException(String message) {
        super(message);
    }
}