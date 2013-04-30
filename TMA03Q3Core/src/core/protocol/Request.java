package core.protocol;

/**
 *
 * @author Edward Wilde
 */
public class Request extends Message
{    
    public Request()
    {
        this(null);
    }

    public Request(String rawRequest)
    {
        super(rawRequest);
    }

    @Override
    public String toStringFirstLine()
    {
        StringBuilder builder = new StringBuilder();
        if (this.getVerb() != null)
        {
            builder.append(this.getVerb().getName()).append(" ");
        }

        this.toStringProtocolAndVersion(builder);

        return builder.toString();
    }
}
