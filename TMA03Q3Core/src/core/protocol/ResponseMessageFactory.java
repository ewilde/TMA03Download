package core.protocol;

import core.protocol.exceptions.MessageException;
import java.io.BufferedReader;

/**
 *
 * @author Edward Wilde
 */
public class ResponseMessageFactory extends MessageFactory implements IResponseFactory
{
    protected Response getResponse()
    {
        return (Response)this.message;
    }

    public Response createResponse(String rawResponse) throws MessageException
    {
        this.message = new Response(rawResponse);
        this.validateNullTermination();

        String[] lines = rawResponse.split(NEWLINE);
        this.parseStartLine(lines[0]);
        this.validateVersion();

        this.parseHeaders(lines);
        this.parseBody();
        return (Response) this.message;
    }

    public Response createResponse(BufferedReader reader) throws MessageException
    {
        String raw = this.readMessageRaw(reader);
        if (raw == null)
        {
            return null;
        }

        return this.createResponse(raw);
    }

    @Override
    protected void parseStartLine(String rawMessage) throws MessageException
    {
        // parse version
        int indexOfFirstSlash = rawMessage.indexOf('/');
        int indexOfFirstSpace = rawMessage.indexOf(" ");
        this.validateMissingVersion(indexOfFirstSlash);

        this.setVersion(rawMessage.substring(indexOfFirstSlash + 1, indexOfFirstSpace - 1));

        // parse status code
        int indexOfSecondSpace = rawMessage.indexOf(" ", indexOfFirstSpace + 1);
        if (indexOfSecondSpace == -1)
        {
            throw new MessageException("Please provide a response status.");
        }

        int status = Integer.parseInt(rawMessage.substring(indexOfFirstSpace + 1,
                                             indexOfSecondSpace));
        
        this.getResponse().setStatusCode(StatusCode.getStatusCode(status));
    }
}
