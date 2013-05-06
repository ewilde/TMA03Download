package client.console;

import client.core.*;
import core.protocol.Request;
import core.protocol.Response;
import core.protocol.exceptions.MessageException;
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Title:        Client console class
 * Description:  Drives the console application handles input from the user
 *               and sends requests to the server.
 * 
 * @author M257 Edward Wilde
 */
public class ClientConsole implements IEncryptionSessionListener
{
    private static final Logger logger = Logger.getLogger(ClientConsole.class.getName());
    private EncryptionSession session;
    private boolean terminated;

    public ClientConsole()
    {
        this.session = new ConsoleSession(this);
    }

    public void run() throws MessageException
    {
        //set up connection to the server
        try
        {
            this.session.connectToServer();
            
            readKeyBoardHelper();

            this.session.closeStreams();
            
        }
        catch (IOException e)
        {
            System.out.println("Exception in client run " + e);
        }
    }

    private void readKeyBoardHelper() throws MessageException
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Keyboard helper is waiting for input (use quit to end)");
        
        terminated = false;
        while(!terminated)
        {
            String next = sc.nextLine().toUpperCase();
            this.session.doCommand(this.session.createClientCommand(next));
        }        
    }

    @Override
    public void onDoQuit()
    {
        this.terminated = true;
    }


    @Override
    public void onWarn(String message)
    {
        logger.log(Level.WARNING, message);
    }

    @Override
    public void onError(String message)
    {
        logger.log(Level.SEVERE, message);
    }
    
    // These methods were added after the console application was written
    // during refactoring for the GUI. If I had time would come back and
    // refactor the console app. Although it still works, just not elegantly.
    @Override
    public void beforeRequestSend(Request request){}

    @Override
    public void afterResponseReceived(Response currentResponse){}    
}