package client.core;


import core.protocol.*;
import core.protocol.exceptions.MessageException;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * Encapsulates core interaction between a client session and the server.
 * @author Edward Wilde
 */
public abstract class EncryptionSession
{
    private static final int SERVER_PORT_NUMBER = 3000;
    private static final Logger logger = Logger.getLogger(EncryptionSession.class.getName());

     //Streams used for communicating with server
    private InputStream is;
    private OutputStream os;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private Socket socket;    // Socket to server

    private IResponseFactory responseFactory;
    private Response currentResponse;
    private boolean connected;
    private final IEncryptionSessionListener listener;

    public EncryptionSession(IEncryptionSessionListener listener)
    {
        this.responseFactory = new ResponseMessageFactory();
        this.connected = false;
        this.listener = listener;
    }
    
    //this method creates a socket on the local host to
    //the specified port number, for communications with the server
    public void connectToServer()
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
            String message = String.format(
                "Trouble contacting the server: %n%s" +
                "Perhaps you need to start the server?%n" +
                "Make sure they're talking on the same port?", e);
            this.listener.onError(message);
        }
    }

     private void openStreams() throws IOException
    {
        final boolean AUTO_FLUSH = true;
        this.is = socket.getInputStream();
        this.fromServer = new BufferedReader(new InputStreamReader(is));
        this.os = socket.getOutputStream();
        this.toServer = new PrintWriter(os, AUTO_FLUSH);
    }

    // close streams to server
    public void closeStreams() throws IOException
    {
        this.toServer.close();
        this.os.close();
        this.fromServer.close();
        this.is.close();
        this.socket.close();
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

    public ClientCommand createClientCommand(String value)
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

    public void doCommand(ClientCommand clientCommand) throws MessageException
    {
        switch(clientCommand)
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
                    if (this.isConnected())
                    {
                        this.doDisconnect();
                    }

                    this.doQuit();
                    break;
                case HELP:
                    this.doHelp();
                    break;
                case UNKNOWN:
                default:
                    this.listener.onError(String.format("Unknown command:%s%n", clientCommand.getName()));
                    break;
            }
    }

    public void doConnect() throws MessageException
    {
        if (this.isConnected())
        {
            this.listener.onWarn("Already connected.");
            return;
        }

        this.doRequestResponse(Verb.CONNECT);
        if (this.currentResponse.getStatusCode() == StatusCode.OK)
        {
            this.connected = true;
        }
    }

    public void doDisconnect() throws MessageException
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

    public void doList() throws MessageException
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

    public abstract void doHelp();
    
    public abstract String GetInputFromUser();
    
    public void doEncrypt() throws MessageException
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

    public void doDecrypt() throws MessageException
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
        request.setCorrelationToken(UUID.randomUUID().toString());
        this.listener.beforeRequestSend(request);

        this.currentResponse = this.SendRequest(request);
        this.currentResponse.setCorrelationToken(request.getCorrelationToken());

        this.HandleResponse(this.currentResponse);
        this.listener.afterResponseReceived(this.currentResponse);
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
        if (!this.isConnected())
        {
            this.listener.onWarn("Please CONNECT first.");
        }

        return this.isConnected();
    }

    /**
     * @return the connected
     */
    public boolean isConnected()
    {
        return connected;
    }

    public void doQuit()
    {
        this.listener.onDoQuit();
    }          
}
