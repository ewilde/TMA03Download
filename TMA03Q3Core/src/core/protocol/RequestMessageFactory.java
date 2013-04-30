/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.protocol;

import core.protocol.exceptions.*;
import java.io.*;
import java.util.logging.*;

/**
 *
 * @author Edward Wilde
 */
public class RequestMessageFactory extends MessageFactory implements IRequestFactory
{
    private static final Logger logger = Logger.getLogger(MessageFactory.class.getName());

    public Request createRequest(String rawRequest) throws InvalidMessageException
    {
        this.message = new Request(rawRequest);
        this.validateNullTermination();
        this.validateVerb();

        String[] lines = rawRequest.split(NEWLINE);
        this.parseStartLine(lines[0]);
        this.validateVersion();

        this.parseHeaders(lines);
        this.parseBody();
        return (Request) this.message;
    }

    public Request createRequest(BufferedReader reader) throws InvalidMessageException
    {
        String raw = this.readMessageRaw(reader);
        if (raw == null)
        {
            return null;
        }
        
        return this.createRequest(raw);
    }

    protected void parseStartLine(String rawRequest) throws InvalidMessageException
    {
        int indexOfSpace = rawRequest.indexOf(' ');
        int indexOfFirstNewLine = rawRequest.indexOf(NEWLINE);
        int indexOfFirstSlash = rawRequest.indexOf('/');

        this.validateMissingVersion(indexOfFirstSlash);

        this.message.setVerb(
                Verb.getVerb(rawRequest.substring(0, indexOfSpace)));
        this.setVersion(
                rawRequest.substring(indexOfFirstSlash + 1,
                indexOfFirstNewLine == -1 ?
                    rawRequest.length() : indexOfFirstNewLine));
    }
    
    protected void validateVerb() throws InvalidMessageException
    {
         // Validate verb is present
        String[] lines = this.message.getRawMessage().split(NEWLINE);
        int indexOfSpace = lines[0].indexOf(' ');
        if (indexOfSpace == -1)
        {
            throw new InvalidMessageException(
                    "Message start with a verb",
                    StatusCode.BAD_REQUEST);
        }     
    }
}
