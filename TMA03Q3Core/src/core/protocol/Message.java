package core.protocol;

import java.util.ArrayList;

/**
 *
 * @author Edward Wilde
 */
public abstract class Message
{
    private final String rawMessage;
    private Verb verb;
    private String body;
    private float version;
    private HeaderCollection headers;

    protected Message()
    {
        this(null);
    }

    protected Message(String rawMessage)
    {
        this.headers = new HeaderCollection();
        this.rawMessage = rawMessage;
        this.setVersion(1.0f);
    }
       
    public Verb getVerb()
    {
        return this.verb;
    }

    public void setVerb(Verb verb)
    {
        this.verb = verb;
    }

    public float getVersion()
    {
        return this.version;
    }

    public String getProtocol()
    {
        return "EPT";
    }

    public void setVersion(float version)
    {
        this.version = version;
    }

    public String getRawMessage()
    {
        return this.rawMessage;
    }

    /**
     * @return the headers
     */
    public HeaderCollection getHeaders() {
        return headers;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    protected void toStringProtocolAndVersion(StringBuilder builder)
    {
        builder.append(this.getProtocol());
        builder.append("/");
        builder.append(this.getVersion());
    }
    

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(this.toStringFirstLine());
        builder.append(MessageFactory.NEWLINE);
        this.toStringHeadersAndBody(builder);
        return builder.toString();
    }

    public abstract String toStringFirstLine();

    protected void toStringHeaders(StringBuilder builder)
    {
        for (Header header : this.getHeaders()) {
            builder.append(String.format("%s%n", header));
        }
    }

    protected void toStringHeadersAndBody(StringBuilder builder)
    {
        this.toStringHeaders(builder);
        
        if (this.getBody() != null) {
            builder.append(String.format("%n%s", this.getBody()));
        }
        builder.append("\u0000");
    }
}
