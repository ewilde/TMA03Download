package tma03q1;

/**
 * Acts as the listener in the telling of a knock knock joke.
 * @author Edward Wilde
 * @see KnockKnock
 * @see Joker
 */
public class Listener implements Runnable
{
    private final static String NAME = "Listener";

    /*
     * Called when a thread runs this object
     */
    @Override
    public void run()
    {
        for (int i=0; i < 4; i++)
        {
            KnockKnock.pause(500);
            this.getJoke();
        }
    }

    /*
     * Response to the current joke being asked.
     */
    private void getJoke()
    {
        System.out.printf("%-10s%s\n", NAME, KnockKnock.getFirstResponse());
        KnockKnock.pause(200);
        System.out.printf("%-10s%s\n", NAME, KnockKnock.getSecondResponse("Don't know"));
    }
}
