package cz.geek.resources;

import cz.geek.GeekMailCopyException;

public class BadResourceException extends GeekMailCopyException {

    public BadResourceException(final String message) {
        super(message);
    }

    public BadResourceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
