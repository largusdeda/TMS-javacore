package moneytransfer.exception;

/**
 * @author Elena Chinarina
 *
 **/

public class SameAccountException extends RuntimeException {
    public SameAccountException(String message) {
        super(message);
    }
}
