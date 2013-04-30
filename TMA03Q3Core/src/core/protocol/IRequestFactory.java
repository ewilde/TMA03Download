/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.protocol;

import core.protocol.exceptions.InvalidMessageException;
import core.protocol.exceptions.MessageException;
import java.io.BufferedReader;

/**
 *
 * @author EddieDesk
 */
public interface IRequestFactory
{
    /**
     * Creates a new request
     * @param rawRequest
     * @return a new request based on the raw message.
     */
    Request createRequest(String rawRequest) throws MessageException;

    Request createRequest(BufferedReader reader) throws InvalidMessageException;
}
