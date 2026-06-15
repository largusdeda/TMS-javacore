package moneytransfer.exception;

/**
 * @author Elena Chinarina
 *
 **/

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
