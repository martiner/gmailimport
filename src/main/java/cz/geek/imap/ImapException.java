package cz.geek.imap;

import cz.geek.GeekMailCopyException;

public class ImapException extends GeekMailCopyException {
    public ImapException(final String message) {
        super(message);
    }

    public ImapException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
