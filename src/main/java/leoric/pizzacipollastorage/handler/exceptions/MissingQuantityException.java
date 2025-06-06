package leoric.pizzacipollastorage.handler.exceptions;

public class MissingQuantityException extends RuntimeException {
    public MissingQuantityException(String message) {
        super(message);
    }
}