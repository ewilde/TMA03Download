package core.protocol;

import core.protocol.exceptions.MessageException;
import java.io.BufferedReader;

/**
 *
 * @author EddieDesk
 */
public interface IResponseFactory
{
    /**
     * Creates a new response
     * @param rawResponse
     * @return a new response based on the raw message.
     */
    Response createResponse(String rawResponse) throws MessageException;

    Response createResponse(BufferedReader reader) throws MessageException;
}
