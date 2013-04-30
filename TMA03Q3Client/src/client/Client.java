package client;

import core.protocol.*;
import core.protocol.exceptions.MessageException;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * Title:        Client class
 * Description:  Skeleton client that connects to a server, receives and displays a
 *               message and then terminates
 * 
 * This class does not have any output streams set up yet, because
 * at the moment it only reads information from the server.
 * This class is based on Activity 9.6
 *
 * There is a method readKeyboardHelper provided to help test drive the server
 * while you are developing the client code.
 *
 * @author M257 Module Team
 */
public class Client
{
    private static final int SERVER_PORT_NUMBER = 3000;
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    //Streams used for communicating with server
    private InputStream is;
    private OutputStream os;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private Socket socket;    // Socket to server

    private IResponseFactory responseFactory;
    private Response currentResponse;
    private boolean connected;
    
    public Client()
    {
        this.responseFactory = new ResponseMessageFactory();
        this.connected = false;
    }

    public void run() throws MessageException
    {
        //set up connection to the server
        try
        {
            connectToServer();

            readKeyBoardHelper();

            closeStreams();
            socket.close();
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
        
        boolean terminated = false;
        while(!terminated)
        {
            String next = sc.nextLine().toUpperCase();
            switch(this.createClientCommand(next))
            {
                case CONNECT:
                    this.doConnect();
                    break;
                case LIST:
                    this.doList();
                    break;
                case DISCONNECT:
                    this.doDisconnect();
                    break;
                case ENCRYPT:
                    this.doEncrypt();
                    break;
                case DECRYPT:
                    this.doDecrypt();
                    break;
                case QUIT:
                    if (this.connected)
                    {
                        this.doDisconnect();
                    }
                    
                    terminated = true;
                    break;
                case HELP:
                    this.doHelp();
                    break;
                case UNKNOWN:
                default:
                    System.out.printf("Unknown command:%s%n", next);
                    break;
            }
        }        
    }

    //this method creates a socket on the local host to
    //the specified port number, for communications with the server
    private void connectToServer()
    {
        try
        {
            //this is a portable way of getting the local host address
            final InetAddress SERVER_ADDRESS = InetAddress.getLocalHost();
            System.out.println("Attempting to contact " + SERVER_ADDRESS);

            socket = new Socket(SERVER_ADDRESS, SERVER_PORT_NUMBER);
            openStreams();
        }
        catch (IOException e)
        {
            String ls = System.getProperty("line.separator");
            System.out.println(ls + "Trouble contacting the server: " + e);
            System.out.println("Perhaps you need to start the server?");
            System.out.println("Make sure they're talking on the same port?" + ls);
        }
    }

    // open streams for communicating with the server
    private void openStreams() throws IOException
    {
        final boolean AUTO_FLUSH = true;
        is = socket.getInputStream();
        fromServer = new BufferedReader(new InputStreamReader(is));
        os = socket.getOutputStream();
        toServer = new PrintWriter(os, AUTO_FLUSH);
    }

    // close streams to server
    private void closeStreams() throws IOException
    {
        toServer.close();
        os.close();
        fromServer.close();
        is.close();
    }

    private Request createRequest(Verb verb)
    {
        Request request = new Request();
        String hostName = "Unknown";
        try
        {
            hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex)
        {
            logger.log(Level.SEVERE, null, ex);
        }

        request.setVerb(verb);
        request.getHeaders().add(new Header("Client-Name", hostName));

        return request;
    }

    private ClientCommand createClientCommand(String value)
    {
        if (value == null || value.length() == 0)
        {
            return ClientCommand.UNKNOWN;
        }

        int indexOfFirstSpace = value.indexOf(" ");

        String commandName = value.substring(0,
                indexOfFirstSpace == -1 ? value.length() : indexOfFirstSpace);

        try
        {
            return ClientCommand.valueOf(commandName);
        }
        catch (IllegalArgumentException exception)
        {
            return ClientCommand.UNKNOWN;
        }
    }

    private void doConnect() throws MessageException
    {
        if (this.connected)
        {
            System.out.println("Already connected.");
            return;
        }

        this.doRequestResponse(Verb.CONNECT);
        if (this.currentResponse.getStatusCode() == StatusCode.OK)
        {
            this.connected = true;
        }
    }

    private void doDisconnect() throws MessageException
    {
        if (!this.checkConnected())
        {
            return;
        }
        
        this.doRequestResponse(Verb.DISCONNECT);

        if (this.currentResponse.getStatusCode() == StatusCode.OK)
        {
            this.connected = false;
        }
    }

    private void doList() throws MessageException
    {
        if (!this.checkConnected())
        {
            return;
        }

       this.doRequestResponse(Verb.LIST);

       if (this.currentResponse.getStatusCode() == StatusCode.OK)
       {
           System.out.println(this.currentResponse.getBody());
       }
    }


    private void doHelp()
    {
        System.out.println(ClientCommand.listCommands());
    }

    private void doEncrypt() throws MessageException
    {
        if (!this.checkConnected())
        {
            return;
        }

        String userInput = this.GetInputFromUser();
        Request request = this.createRequest(Verb.ENCRYPT);
        request.setBody(userInput);
        this.doRequestResponse(request);

        if (this.currentResponse.getStatusCode() == StatusCode.OK)
        {
            System.out.println(this.currentResponse.getBody());
        }
    }

    private void doDecrypt() throws MessageException
    {
        if (!this.checkConnected())
        {
            return;
        }
        
        String userInput = this.GetInputFromUser();
        Request request = this.createRequest(Verb.DECRYPT);
        request.setBody(userInput);
        this.doRequestResponse(request);

        if (this.currentResponse.getStatusCode() == StatusCode.OK)
        {
            System.out.println(this.currentResponse.getBody());
        }
    }

    private void doRequestResponse(Verb verb) throws MessageException
    {
        this.doRequestResponse(this.createRequest(verb));
    }

    private void doRequestResponse(Request request) throws MessageException
    {
        this.currentResponse = this.SendRequest(request);
        this.HandleResponse(this.currentResponse);
    }

    private Response SendRequest(Request request) throws MessageException
    {
        toServer.print(String.format("%s%n", request.toString()));
        toServer.flush();
        logger.log(Level.INFO, String.format("Send request: %s%n", 
                request.toStringFirstLine()));
                
        return responseFactory.createResponse(fromServer);
    }

    private void HandleResponse(Response response)
    {
        if (response.getStatusCode() != StatusCode.OK)
        {
            logger.log(Level.SEVERE, String.format("Error %s%n%s",
                    response.getStatusCode(),
                    response.getBody()));
        }
        else
        {
            logger.log(Level.INFO, response.toStringFirstLine());
        }
    }

    private boolean checkConnected()
    {
        if (!this.connected)
        {
            System.out.println("Please CONNECT first.");
        }

        return this.connected;
    }

    private String GetInputFromUser()
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