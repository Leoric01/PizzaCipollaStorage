package leoric.pizzacipollastorage.handler.exceptions;

public class InsufficientRoleException extends RuntimeException {
    public InsufficientRoleException(String message) {
        super(message);
    }
}