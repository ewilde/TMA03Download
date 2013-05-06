package client;

import client.console.ClientConsole;
import client.ui.Application;
import core.protocol.exceptions.MessageException;
import java.util.logging.*;

/**
 * Title:        StartClient class
 * Description:  Runs the console or UI application.

 * @author Edward Wilde
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
            try
            {
                console.run();
            }
            catch (MessageException ex)
            {
                Logger.getLogger(StartClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run()
                {
                    Application application = new Application();
                    application.setVisible(true);
                }
            });
        }
   }
}
