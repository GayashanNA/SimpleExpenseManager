package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception;

/**
 *
 */
public class InvalidAccountException extends Exception {
    public InvalidAccountException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidAccountException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
