package cz.geek;

/**
 * Created by martin on 01/02/15.
 */
public class GeekMailCopyException extends RuntimeException {
    public GeekMailCopyException(final String message) {
        super(message);
    }

    public GeekMailCopyException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
