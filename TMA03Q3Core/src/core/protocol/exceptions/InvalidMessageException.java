package core.protocol.exceptions;

import core.protocol.StatusCode;

/**
 *
 * @author Edward Wilde
 */
public class InvalidMessageException extends MessageException
{
    private final StatusCode statusCode;

    public InvalidMessageException(String message, StatusCode statusCode)
    {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * @return the statusCode
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }
}
