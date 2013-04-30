/*
 * KnockKnock models the parts of a knock knock joke.
 * Static methods and constants model the fixed parts of the joke,
 * while instance methods model the variable parts.
 */
package tma03q1;

/**
 *
 * @author M257 Module Team
 * Amended 09/08/12 - Added pause method.
 */

public class KnockKnock
{
    private static final String KNOCK_KNOCK = "Knock knock!";
    private static final String WHO_IS_THERE = "Who's there?";
    private String setup;
    private static final String WHO = "who?";
    private String punchline;

    /**
     * Construct a knock knock joke with a particular setup and punchline
     * @param aName the setup, usually someone's name
     * @param aPunchline the punchline
     */
    public KnockKnock(String aName, String aPunchline)
    {
        setup = aName;
        punchline = aPunchline;
    }


    /**
     * pause the currently executing thread for millis
     * @param millis the number of milliseconds to pause for
     */
    public static void pause(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException ie)
        {
            System.out.println(ie);
        }
    }



    /**
     * @return the standard opening line of a knock knock joke
     */
    public static String getJokeStart()
    {
        return KNOCK_KNOCK;
    }

    /**
     * @return the standard response to the opening line
     */
    public static String getFirstResponse()
    {
        return WHO_IS_THERE;
    }

    /**
     * @return the setup, usually someone's name
     */
    public String getSetup()
    {
        return setup;
    }

    /**
     * Provide a response to the setup
     * @param someone the setup the joker used
     * @return the setup + the string "who?"
     */
    public static String getSecondResponse(String someone)
    {
        return someone + WHO;
    }

    /**
     * @return the punchline for this joke
     */
    public String getPunchline()
    {
        return punchline;
    }
}
