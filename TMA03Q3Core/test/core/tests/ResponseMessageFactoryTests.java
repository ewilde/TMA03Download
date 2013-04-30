package core.tests;


import core.protocol.*;
import core.protocol.exceptions.*;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Tests the response message factory. Not most test are for the factory are in
 * RequestMessageFactoryTests
 *
 * @author Edward Wilde
 */
public class ResponseMessageFactoryTests
{
     public static final String MULTLINE_LINE_RESPONSE = String.format(
            "EPT/1.0 200 OK%n" +
            "Client-Name: Frodo%n" +
            "Algorithm: DES%n" +
            "%n" +
            "Body of message\u0000");

    @Test
    public void Version_is_parsed() throws MessageException
    {
        ResponseMessageFactory messageFactory = new ResponseMessageFactory();

        Response response = messageFactory.createResponse(MULTLINE_LINE_RESPONSE);

        assertThat(response.getVersion(), is(1.0f));
    }

    @Test
    public void Status_code_is_parsed() throws MessageException
    {
        ResponseMessageFactory messageFactory = new ResponseMessageFactory();

        Response response = messageFactory.createResponse(MULTLINE_LINE_RESPONSE);

        assertThat(response.getStatusCode(), is(StatusCode.OK));
    }
}
