package server;

import java.util.logging.*;


/**
 * Title:        StartServer class
 * Description:  Tests the Server class

 * @author M257 Course Team
 */

public class StartServer
{
   // Create a server and have it greet the client
   public static void main(String[] args)
   {
        Server server1 = new Server();
        server1.run();
   }
}
