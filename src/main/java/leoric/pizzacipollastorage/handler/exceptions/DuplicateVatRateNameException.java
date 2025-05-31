package leoric.pizzacipollastorage.handler.exceptions;

public class DuplicateVatRateNameException extends RuntimeException {
    public DuplicateVatRateNameException(String message) {
        super(message);
    }
}