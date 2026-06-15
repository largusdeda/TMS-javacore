package moneytransfer.exception;

/**
 * @author Elena Chinarina
 *
 **/

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
