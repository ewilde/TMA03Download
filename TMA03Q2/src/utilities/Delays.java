/*
 * Delays.java
 *
 * Convenience class: provides static methods for introducing a delay.
 */

package utilities;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author M257
 */

// You do not need to change this class.
public final class Delays
{
    
    /* Objects of this class should never be created.*/
    private Delays()
    {
    }
    
    public static void delay(int ms)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(ms);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void randomDelay(int ms)
    {
        Random random = new Random();
        try
        {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(ms));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
