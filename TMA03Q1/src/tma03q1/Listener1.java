package tma03q1;

/**
 * Acts as the listener in the telling of a knock knock joke.
 * @author Edward Wilde
 * @see KnockKnock
 * @see Joker
 */
public class Listener1 implements Runnable
{
    private final static String NAME = "Listener";

    private Joker joker;

    /**
     * @param joker telling the joke.
     */
    public Listener1(Joker joker)
    {
        this.joker = joker;
    }

    /**
     * Wait until it's the listeners turn to proceed.
     */
    private void waitForMyTurn()
    {
        while(KnockKnock1.isJokersTurn())
        {
            KnockKnock1.pause(100);
        }
    }

    /*
     * Called when a thread runs this object
     */
    @Override
    public void run()
    {
        for (int i=0; i < this.joker.getNumOfJokes(); i++)
        {
            this.getJoke();
        }
    }

    /*
     * Response to the current joke being asked.
     */
    private void getJoke()
    {
        this.getJokeLine(KnockKnock.getFirstResponse());
        this.getJokeLine(KnockKnock.getSecondResponse(this.joker.getSetup()));
    }

    private void getJokeLine(String line)
    {
        this.waitForMyTurn();
        System.out.printf("%-10s%s\n", NAME, line);
        KnockKnock1.setJokersTurn(true);
    }
}
