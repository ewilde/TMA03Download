/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.core;

import core.protocol.Request;
import core.protocol.Response;

/**
 *
 * @author EddieDesk
 */
public interface IEncryptionSessionListener
{
    void onDoQuit();

    void beforeRequestSend(Request request);

    void afterResponseReceived(Response response);
}
