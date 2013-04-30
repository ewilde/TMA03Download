package core.protocol;

import java.util.*;

/**
 *
 * @author Edward Wilde
 */
public class HeaderCollection extends ArrayList<Header>
{
    public Header get(String headerName)
    {
        for(Header header : this)
        {
            if (header.getName().equals(headerName))
            {
                return header;
            }
        }

        return null;
    }
}
