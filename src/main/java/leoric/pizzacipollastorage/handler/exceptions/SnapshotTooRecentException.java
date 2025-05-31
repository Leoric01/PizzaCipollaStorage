package leoric.pizzacipollastorage.handler.exceptions;

public class SnapshotTooRecentException extends RuntimeException {
    public SnapshotTooRecentException(String message) {
        super(message);
    }
}