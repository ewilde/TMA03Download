package server;

import core.protocol.*;
import core.protocol.exceptions.*;
import core.security.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.logging.*;

/**
 * Title:        Server class
 * Description:  Responsible for handling client connections.
 *
 *
 * @author M257 Edward Wilde
 */
public class Server
{
    private ServerSocket serverSocket;
    private Socket socket;    

    // use a high numbered non-dedicated port
    private static final int PORT_NUMBER = 3000;
    private static final String MESSAGE_TO_CLIENT = "Hello client. This is the server.";
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // constructor
    public Server()
    {
        System.out.println("...Server starting up");        
        try
        {
            // create a ServerSocket object to listen on the port
            serverSocket = new ServerSocket(PORT_NUMBER);
        }
        catch (IOException e)
        {
            System.out.println("Trouble with ServerSocket on port " + PORT_NUMBER
                    + ": " + e);
        }
    } // end constructor

    //This method is used to start the server and perform a single
    //interaction with a single client using the processHello method.
    public void run()
    {
        try
        {
            while(true)
            {
                // wait for a connection request
                socket = serverSocket.accept();
                System.out.println("...Connection established");
                new Thread(new EncryptionSession(socket)).start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Trouble with a connection " + e);
        }
    }

   
} // end class

