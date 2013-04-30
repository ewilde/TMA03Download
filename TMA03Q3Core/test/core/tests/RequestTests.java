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
public class RequestTests
{
    @Test
    public void to_string_ends_in_null_terminator()
    {
        Request request = new Request();

        assertThat(request.toString().endsWith("\u0000"), is(true));
    }

    @Test
    public void to_string_returns_verb()
    {
        Request request = new Request();
        request.setVerb(Verb.CONNECT);

        assertThat(request.toString().startsWith(Verb.CONNECT.getName()),
                is(true));
    }

    @Test
    public void to_string_returns_protocol()
    {
        Request request = new Request();
        request.setVerb(Verb.CONNECT);

        assertThat(request.toString().contains("EPT"),
                is(true));
    }

    @Test
    public void to_string_returns_version()
    {
        Request request = new Request();
        request.setVerb(Verb.CONNECT);

        assertThat(request.toString().contains("1.0"),
                is(true));
    }

    @Test
    public void to_string_returns_headers()
    {
        Request request = new Request();
        request.setVerb(Verb.CONNECT);

        request.getHeaders().add(new Header("Client-Name", "Frodo"));
        request.getHeaders().add(new Header("Algorithm", "Blowfish"));

        assertThat(request.toString().contains("Client-Name: Frodo"),
                is(true));

        assertThat(request.toString().contains("Algorithm: Blowfish"),
                is(true));
    }

    @Test
    public void to_string_returns_body()
    {
        Request request = new Request();
        request.setVerb(Verb.CONNECT);

        request.getHeaders().add(new Header("Client-Name", "Frodo"));
        request.getHeaders().add(new Header("Algorithm", "Blowfish"));

        request.setBody("Body of message");

        assertThat(request.toString().contains("Body of message"), is(true));
    }

    @Test
    public void to_string_format_matches_exactly()
    {
        Request request = new Request();
        request.setVerb(Verb.CONNECT);

        request.getHeaders().add(new Header("Client-Name", "Frodo"));
        request.getHeaders().add(new Header("Algorithm", "DES"));

        request.setBody("Body of message");

        assertThat(request.toString(),
                equalTo(RequestMessageFactoryTests.MULTLINE_LINE_REQUEST));
    }
}
