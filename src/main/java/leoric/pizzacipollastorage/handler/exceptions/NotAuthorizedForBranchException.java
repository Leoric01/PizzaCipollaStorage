package leoric.pizzacipollastorage.handler.exceptions;

public class NotAuthorizedForBranchException extends RuntimeException {
    public NotAuthorizedForBranchException() {
        super("You are not authorized to manage this branch");
    }

    public NotAuthorizedForBranchException(String message) {
        super(message);
    }
}