package client;

/**
 *
 * @author Edward Wilde
 */
public enum ClientCommand
{
    CONNECT("CONNECT", "Connects to the server"),
    LIST("LIST", "Lists all types of encryption methods the server supports."),
    ENCRYPT("ENCRYPT", "Encrypt {Text to encrypt}."),
    DECRYPT("DECRYPT", "Decrypt {Text to decrypt}."),
    DISCONNECT("DISCONNECT", "Disconnects from the server."),
    HELP("HELP", "Displays list of commands that can be used."),
    QUIT("QUIT", "Disconnects from the server if connected and ends this session."),
    UNKNOWN(null, null);

    private final String name;
    private final String description;

    private ClientCommand(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    public static String listCommands()
    {
        StringBuilder builder = new StringBuilder();
        for(ClientCommand command : values())
        {
            if (command != UNKNOWN)
            {
                builder.append(String.format("%-12s%s\n",
                    command.getName(), command.getDescription()));
            }
        }

        return builder.toString();
    }
}
