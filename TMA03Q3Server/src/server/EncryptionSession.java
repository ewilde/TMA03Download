package server;

import core.protocol.*;
import core.protocol.exceptions.*;
import core.security.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edward Wilde
 */
public class EncryptionSession implements Runnable
{

    // streams for communication to client
    private InputStream is;
    private OutputStream os;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private IRequestFactory requestFactory;
    private Request currentRequest;
    private boolean connected;
    private final Socket socket;

    private static final Logger logger = Logger.getLogger(EncryptionSession.class.getName());

    public EncryptionSession(Socket socket)
    {
        this.socket = socket;
        this.requestFactory = new RequestMessageFactory();        
    }

    @Override
    public void run()
    {
        try
        {
            openStreams();
            processClientRequest();
            closeStreams();
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(EncryptionSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openStreams() throws IOException
    {
        final boolean AUTO_FLUSH = true;
        is = socket.getInputStream();
        fromClient = new BufferedReader(new InputStreamReader(is));
        os = socket.getOutputStream();
        toClient = new PrintWriter(os, AUTO_FLUSH);
        System.out.println("...Streams set up");
    }

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
        String hostName = "Unknown";
        try
        {
            hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex)
        {
        }

        Response response = new Response();
        response.setStatusCode(statusCode);
        response.getHeaders().add(new Header("Server-Name", hostName));

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
}
