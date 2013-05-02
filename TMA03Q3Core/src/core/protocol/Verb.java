package core.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Edward Wilde
 */
public enum Verb
{
    CONNECT("CONNECT", "Create a new encryption session."),
    DISCONNECT("DISCONNECT", "End an encryption session."),
    LIST("LIST", "Lists the supported algorithms"),
    ENCRYPT("ENCRYPT", "Encrypts the request body"),
    DECRYPT("DECRYPT", "Decrypts the request body");

    private final String name;
    private final String description;
    private static final Map<String, Verb> nameToVerbMapping;

    static
    {
        nameToVerbMapping = new HashMap<String, Verb>(values().length);
        for(Verb verb : values())
        {
            nameToVerbMapping.put(verb.name, verb);
        }
    }

    private Verb(String name, String description)
    {
        this.name = name;
        this.description = description;

    }

    public static Verb getVerb(String value) throws IllegalArgumentException
    {
        if (!nameToVerbMapping.containsKey(value))
        {
            throw new IllegalArgumentException(
                    String.format("Value %s not a known verb", value));
        }

        return nameToVerbMapping.get(value);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
