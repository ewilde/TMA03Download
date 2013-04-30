/*
 * FerryConductor.java
 *
 */

package tma03q2;

import static utilities.Delays.*;

/**
 *
 * @author M257
 */

// You do not need to change this class.
public class FerryConductor extends Thread
{
    private Ferry ferry;

    public FerryConductor(Ferry aFerry)
    {
        ferry = aFerry;
    }
    
    public void run()
    {
        for (int i = 0; i < 10; i++)
        {
            delay(2000);
            System.out.println("Ferry trip: " + (i + 1));
            ferry.cross();
        }
    }
    
}
