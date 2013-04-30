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
public class ResponseTests
{
    @Test
    public void to_string_returns_status_code()
    {
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);

        assertThat(response.toString().contains("200 OK"), is(true));
    }

    @Test
    public void to_string_returns_starts_with_protocol_and_version()
    {
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);

        assertThat(response.toString().startsWith("EPT/1.0 "),
                is(true));
    }

    @Test
    public void to_string_returns_headers()
    {
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.getHeaders().add(new Header("foo", "bar"));

        assertThat(response.toString().contains("foo: bar"),
                is(true));
    }

    @Test
    public void to_string_returns_body()
    {
        Response response = new Response();
        response.setStatusCode(StatusCode.OK);
        response.getHeaders().add(new Header("foo", "bar"));
        response.setBody("Response body");

        assertThat(response.toString()
                    .contains(String.format("%n%n%s", "Response body\u0000")),
                    is(true));
    }
}
