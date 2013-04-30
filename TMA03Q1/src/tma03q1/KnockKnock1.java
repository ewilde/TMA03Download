package tma03q1;

/**
 *
 * @author Edward Wilde
 */
public class KnockKnock1 extends KnockKnock
{
    private static boolean jokersTurn = true;

    /**
     * Construct a knock knock joke with a particular setup and punchline
     * @param aName the setup, usually someone's name
     * @param aPunchline the punchline
     */
    public KnockKnock1(String aName, String aPunchline)
    {
        super(aName, aPunchline);
    }

    /**
     * @return the jokersTurn
     */
    public static synchronized boolean isJokersTurn() {
        return jokersTurn;
    }

    /**
     * @param aJokersTurn the jokersTurn to set
     */
    public static synchronized void setJokersTurn(boolean aJokersTurn) {
        jokersTurn = aJokersTurn;
    }
}
