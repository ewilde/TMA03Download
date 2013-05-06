package core.protocol;

import core.protocol.exceptions.InvalidMessageException;
import core.protocol.exceptions.MessageException;
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author Edward Wilde
 */
public abstract class MessageFactory
{
    protected Message message;
    protected static final String END_OF_MESSAGE = "\u0000";
    protected static final String NEWLINE = System.getProperty("line.separator");
    protected static final ArrayList<Float> supportedVersions;
    private static final Logger logger = Logger.getLogger(MessageFactory.class.getName());
    static
    {
        supportedVersions = new ArrayList<Float>();
        supportedVersions.add(1.0f);
    }

    protected String readMessageRaw(BufferedReader reader)
    {
        StringBuilder builder = new StringBuilder();
        String buffer;

        try {
            while (!(buffer = reader.readLine()).endsWith("\u0000"))
            {
                builder.append(String.format("%s%n", buffer));
            }

            // read off any data left after termination
            while (reader.ready())
            {
                reader.read();
            }

            builder.append(buffer);

        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }

        String result = builder.toString();
        logger.log(Level.INFO, String.format("Read raw message: %s", result));
        return result;
    }

    protected void parseBody()
    {
        String raw = this.message.getRawMessage();
        int indexOfBlankline = raw.indexOf(RequestMessageFactory.NEWLINE + RequestMessageFactory.NEWLINE);
        if (indexOfBlankline == -1) {
            return;
        }
        this.message.setBody(raw.substring(indexOfBlankline + (RequestMessageFactory.NEWLINE + RequestMessageFactory.NEWLINE).length(), raw.length() - 1)); // Trim off the null terminator
    }

    protected void parseHeaders(String[] lines)
    {
        for (int i = 1; i < lines.length; i++) // Start at 1, first line is not headers
        {
            String line = lines[i];
            if (line.equals(RequestMessageFactory.NEWLINE) ||
                line.equals(RequestMessageFactory.END_OF_MESSAGE) ||
                line.equals(""))
            {
                break;
            }
            String[] headerPairs = line.split(":");
            this.message.getHeaders().add(new Header(headerPairs[0].trim(), headerPairs[1].trim()));
        }
    }

    protected abstract void parseStartLine(String rawMessage) throws MessageException;

    protected void validateNullTermination() throws InvalidMessageException
    {
        // Validate null termination
        if (this.message.getRawMessage() == null || !this.message.getRawMessage().endsWith("\u0000"))
        {
            throw new InvalidMessageException("Message must end with a null terminating character", StatusCode.BAD_REQUEST);
        }
    }

    protected void validateVersion() throws InvalidMessageException
    {
        // Supported protocol versions
        if (!RequestMessageFactory.supportedVersions.contains(this.message.getVersion()))
        {
            throw new InvalidMessageException("Version of EPT not supported", StatusCode.VERSION_NOT_SUPPORTED);
        }
    }

    protected void setVersion(String value) throws InvalidMessageException
    {
        try
        {
            this.message.setVersion(Float.parseFloat(value));
        } catch (NumberFormatException exception) {
            throw new InvalidMessageException("Please provide a valid version number", StatusCode.BAD_REQUEST);
        }
    }

    protected void validateMissingVersion(int indexOfFirstSlash) throws InvalidMessageException
    {
        if (indexOfFirstSlash == -1)
        {
            throw new InvalidMessageException("Please provide a version", StatusCode.BAD_REQUEST);
        }
    }

}
