package core.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Edward Wilde
 */
public enum StatusCode
{
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad request"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    VERSION_NOT_SUPPORTED(505, "EPT Version not supported");

    private static final Map<Integer, StatusCode> codeToStatusCodeMapping;
    private final int code;
    private final String description;

    static
    {
        codeToStatusCodeMapping = new HashMap<Integer, StatusCode>(values().length);
        for(StatusCode statusCode : values())
        {
            codeToStatusCodeMapping.put(statusCode.code, statusCode);
        }
    }
    
    private StatusCode(int code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public static StatusCode getStatusCode(int value)
    {
        if (!codeToStatusCodeMapping.containsKey(value))
        {
            throw new IllegalArgumentException(
                    String.format("Code %s not a known status code", value));
        }

        return codeToStatusCodeMapping.get(value);
    }


    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", this.getCode(), this.getDescription());
    }
}
