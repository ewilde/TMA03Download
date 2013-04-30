package core.protocol.exceptions;

import core.protocol.StatusCode;

/**
 *
 * @author Edward Wilde
 */
public class MessageException extends Exception
{
    public MessageException(String message)
    {
        super(message);        
    }    
}
