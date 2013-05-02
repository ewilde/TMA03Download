package client;

import client.console.ClientConsole;
import client.ui.Application;
import core.protocol.exceptions.MessageException;
import java.util.logging.*;

/**
 * Title:        StartClient class
 * Description:  Tests the Client class

 * @author M257 Module Team
 */

public class StartClient
{
   public static void main(String[] args) throws MessageException
   {
        boolean loadUI = false;
        if (args.length > 0)
        {
            if (args[0].equals("--ui"))
            {
                loadUI = true;
            }
        }

        if (!loadUI)
        {
            ClientConsole console = new ClientConsole();
            console.run();
        }
        else
        {
            Application application = new Application();
            application.setVisible(true);
        }
   }
}
