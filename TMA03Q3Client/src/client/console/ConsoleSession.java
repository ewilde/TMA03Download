package client.console;

import client.core.*;
import java.util.*;

/**
 *
 * @author Edward Wilde
 */
public class ConsoleSession extends EncryptionSession
{
    public ConsoleSession(IEncryptionSessionListener listener)
    {
        super(listener);
    }
    
    @Override
    public void doHelp()
    {
        System.out.println(ClientCommand.listCommands());
    }

    @Override
    public String GetInputFromUser()
    {

        System.out.println("Please enter text. To end enter blank line:");

        StringBuilder clearText = new StringBuilder();
        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            String line = scanner.nextLine();
            if (line.equals(""))
            {
                break;
            }

            clearText.append(String.format("%s%n", line));
        }

        return clearText.toString();
    }
}
