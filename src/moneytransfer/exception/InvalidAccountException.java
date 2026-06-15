package moneytransfer.exception;

/**
 * @author Elena Chinarina
 *
 **/

public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(String message) {
        super(message);
    }
}