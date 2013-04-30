package core.tests;

import core.protocol.*;
import core.protocol.exceptions.*;
import org.junit.runners.JUnit4;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
/**
 *
 * @author Edward Wilde
 */
@RunWith(JUnit4.class)
public class RequestMessageFactoryTests {
    public static final String SIMPLE_CONNECT = "CONNECT EPT/1.0\u0000";
    public static final String MULTLINE_LINE_REQUEST = String.format(
            "CONNECT EPT/1.0%n" +
            "Client-Name: Frodo%n" +
            "Algorithm: DES%n" +
            "%n" +
            "Body of message\u0000");

    @Test
    public void should_parse_verb() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(SIMPLE_CONNECT);
        
        assertThat(request.getVerb(),is(Verb.CONNECT));
    }

    @Test
    public void should_parse_protocol_version() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(SIMPLE_CONNECT);

        assertThat(request.getVersion(),is(1.0f));
    }

    @Test
    public void should_parse_protocol_version_with_multiline_request() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(MULTLINE_LINE_REQUEST);

        assertThat(request.getVersion(),is(1.0f));
    }

    @Test
    public void should_parse_headers_count() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(MULTLINE_LINE_REQUEST);

        assertThat(request.getHeaders().size(),is(2));
    }

    @Test
    public void should_parse_header_name() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(MULTLINE_LINE_REQUEST);

        assertThat(request.getHeaders().get(0).getName(),is("Client-Name"));
    }

    @Test
    public void should_parse_header_value() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(MULTLINE_LINE_REQUEST);

        assertThat(request.getHeaders().get(1).getValue(),is("DES"));
    }

    @Test
    public void should_parse_request_body() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest(MULTLINE_LINE_REQUEST);

        assertThat(request.getBody(), is("Body of message"));
    }

    @Test(expected=InvalidMessageException.class)
    public void request_without_null_termination_throws_invalid_message_exception() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest("CONNECT EPT/1.0");
    }

    @Test(expected=InvalidMessageException.class)
    public void request_without_verb_invalid_message_exception() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest("EPT/1.0\u0000");
    }

    @Test(expected=InvalidMessageException.class)
    public void request_without_version_throws_invalid_message_exception() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest("CONNECT EPT\u0000");
    }

    @Test(expected=InvalidMessageException.class)
    public void request_with_invalid_version_throws_invalid_message_exception() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest("CONNECT EPT/3z\u0000");
    }

    @Test(expected=InvalidMessageException.class) 
    public void request_with_unsupport_version() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest("CONNECT EPT/0.9\u0000");
    }

    @Test(expected=InvalidMessageException.class)
    public void request_that_is_empty_throws_invalid_message_exception() throws InvalidMessageException
    {
        Request request = new RequestMessageFactory().createRequest("");
    }
}
