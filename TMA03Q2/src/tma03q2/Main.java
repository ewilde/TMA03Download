/*
 * Main.java
 *
 */

package tma03q2;

import static utilities.Delays.*;
import tma03q2.Passenger;

import static utilities.Direction.*;

/**
 *
 * @author M257
 */

// The only change you need to make to this class is to alter the
// length of the random delay.
public class Main
{
    public static void main(String[] args)
    {
        int randomDelayLength = 500;
        
        Ferry ferry = new Ferry();
        FerryConductor ferryman = new FerryConductor(ferry);
        ferryman.start();
        for (int i = 1; i <= 10; i = i + 2)
        {
            randomDelay(randomDelayLength);
            (new Passenger(i, EASTBOUND, ferry)).start();
            randomDelay(randomDelayLength);
            (new Passenger(i + 1, WESTBOUND, ferry)).start();
        }
        
    }
    
}
