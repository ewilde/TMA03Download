/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.core;

import core.protocol.Request;
import core.protocol.Response;

/**
 * Client application should implement this interface to respond to
 * interactions during an encryption session.
 * @author EddieDesk
 */
public interface IEncryptionSessionListener
{
    /**
     * Inform the interface of a warning condition
     * @param message containing the warning.
     */
    void onWarn(String message);

    /**
     * Inform the user interface of an error condition.
     * @param messagecontaiing the error condition.
     */
    void onError(String message);

    /**
     * Inform the user interface that the quit command has been
     * send to the server and the client application should now quit.
     */
    void onDoQuit();

    /**
     * Called before a request is sent to the server
     * @param request to be sent to the server.
     */
    void beforeRequestSend(Request request);

    /**
     * Called after a response has been received from the server.
     * @param response received.
     */
    void afterResponseReceived(Response response);
}
