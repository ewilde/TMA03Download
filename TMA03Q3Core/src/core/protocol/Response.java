package core.protocol;

/**
 *
 * @author Edward Wilde
 */
public class Response extends Message
{
    private StatusCode statusCode;

    public Response()
    {
        this(null);
    }

    public Response(String rawResponse)
    {
        super(rawResponse);
    }
   
    @Override
    public String toStringFirstLine()
    {
        StringBuilder builder = new StringBuilder();
        
        this.toStringProtocolAndVersion(builder);

        if (this.statusCode != null)
        {
            builder.append(String.format(" %s %s",
                    this.statusCode.getCode(),
                    this.statusCode.getDescription()));
        }

        
        return builder.toString();
    }

    /**
     * @return the statusCode
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String toStringHeaders()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
