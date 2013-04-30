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
    
    // streams for communication to client
    private InputStream is;
    private OutputStream os;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private IResponseFactory responseFactory;
    private IRequestFactory requestFactory;
    private Request currentRequest;
    private boolean connected;

    // use a high numbered non-dedicated port
    private static final int PORT_NUMBER = 3000;
    private static final String MESSAGE_TO_CLIENT = "Hello client. This is the server.";
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // constructor
    public Server()
    {
        System.out.println("...Server starting up");
        this.requestFactory = new RequestMessageFactory();
        this.responseFactory = new ResponseMessageFactory();
        
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
            // wait for a connection request
            socket = serverSocket.accept();
            System.out.println("...Connection established");

            openStreams();

            processClientRequest();

            closeStreams();
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Trouble with a connection " + e);
        }
    }

    // set up streams for communicating with the client
    private void openStreams() throws IOException
    {
        final boolean AUTO_FLUSH = true;
        is = socket.getInputStream();
        fromClient = new BufferedReader(new InputStreamReader(is));
        os = socket.getOutputStream();
        toClient = new PrintWriter(os, AUTO_FLUSH);
        System.out.println("...Streams set up");
    }

    // close output streams to client
    private void closeStreams() throws IOException
    {
        toClient.close();
        os.close();
        fromClient.close();
        is.close();
        System.out.println("...Streams closed down");
    }

    private void processClientRequest()
    {
        boolean receivedDisconnectCommand = false;
        boolean errorOccured = false;

        while(!receivedDisconnectCommand && !errorOccured)
        {
            try
            {
                this.currentRequest = this.requestFactory.createRequest(fromClient);
                if (this.currentRequest == null)
                {
                    logger.log(Level.SEVERE,
                            "Error parsing request, will disconnect client.");
                    errorOccured = true;
                    continue;
                }

                logger.log(Level.INFO,
                        String.format("Request recieved from client %s.",
                        this.currentRequest.toString()));

                switch(this.currentRequest.getVerb())
                {
                    case CONNECT:
                        this.doConnect();
                        break;
                    case DISCONNECT:
                        this.doDisconnect();
                        receivedDisconnectCommand = true;
                        break;
                    case LIST:
                        this.doList();
                        break;
                    case ENCRYPT:
                        this.doEncrypt();
                        break;
                    case DECRYPT:
                        this.doDecrypt();
                        break;
                    default:
                        errorOccured = false;
                        throw new InvalidMessageException(
                                String.format("Verb %s not supported.",
                                    this.currentRequest.getVerb()),
                                StatusCode.METHOD_NOT_ALLOWED);                        
                }
            }
            catch (InvalidMessageException ex)
            {
                logger.log(Level.SEVERE, null, ex);
                this.sendResponse(this.createResponse(StatusCode.BAD_REQUEST,
                        ex.getMessage()));
            }
        }
    }

    private void doConnect()
    {
        this.setConnected(true);
        this.sendResponse(this.createResponse(StatusCode.OK, null));
    }

    private void doDisconnect()
    {
        this.setConnected(false);
        this.sendResponse(this.createResponse(StatusCode.OK, null));
    }

    private void doList()
    {
        this.sendResponse(this.createResponse(StatusCode.OK,
                EncryptionAlgorithm.listEncryptionAlgorithms()));
    }

    private void doEncrypt()
    {
        Header header = this.currentRequest.getHeaders().get("Algorithm");
        EncryptionAlgorithm algorithm;

        if (header == null)
        {
            algorithm = EncryptionAlgorithm.PBE;
        }
        else
        {
            algorithm = EncryptionAlgorithm.valueOf(header.getValue());
        }

        EncryptionEngine engine = new EncryptionEngine(algorithm);
        String cipherText;
        try
        {
            cipherText = engine.Encrypt(algorithm.getName(), this.currentRequest.getBody());
            this.sendResponse(this.createResponse(StatusCode.OK, cipherText));
        }
        catch(Exception exception)
        {
            this.sendResponse(this.createResponse(StatusCode.BAD_REQUEST, exception.toString()));
        }
    }

    private void doDecrypt()
    {
        Header header = this.currentRequest.getHeaders().get("Algorithm");
        EncryptionAlgorithm algorithm;

        if (header == null)
        {
            algorithm = EncryptionAlgorithm.PBE;
        }
        else
        {
            algorithm = EncryptionAlgorithm.valueOf(header.getValue());
        }

        EncryptionEngine engine = new EncryptionEngine(algorithm);
        String cipherText;
        try
        {
            cipherText = engine.Decrypt(algorithm.getName(), this.currentRequest.getBody());
            this.sendResponse(this.createResponse(StatusCode.OK, cipherText));
        }
        catch(Exception exception)
        {
            this.sendResponse(this.createResponse(StatusCode.BAD_REQUEST, exception.toString()));
        }
    }

    public void sendResponse(Response response)
    {
        logger.log(Level.INFO, String.format("Sending response %s", response));
        this.toClient.print(String.format("%s%n",response));
        this.toClient.flush();
    }


    private Response createResponse(StatusCode statusCode, String body)
    {
        Response response = new Response();
        response.setStatusCode(statusCode);

        if (body != null)
        {
            response.setBody(body);
        }
        
        return response;
    }

    /**
     * @return the connected
     */
    public boolean isConnected()
    {
        return connected;
    }

    /**
     * @param connected the connected to set
     */
    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }
} // end class

