package tma03q1;

/**
 *
 * @author Edward Wilde
 */
public class Joker1 extends Joker
{
    /**
     * Wait until it's the jokers turn to proceed.
     */
    private void waitForMyTurn()
    {
        while(!KnockKnock1.isJokersTurn())
        {
            KnockKnock1.pause(100);
        }
    }

    /**
     * Tells a line of the joke, by waiting first until it's the jokers turn.
     * @param line of the joke to tell.
     * @param changeTurn if true, changes the turn to enable a reply.
     */
    private void tellJokeLine(String line, boolean changeTurn)
    {
        this.waitForMyTurn();
        System.out.printf("%-10s%s\n", NAME, line);

        if (changeTurn)
        {
            KnockKnock1.setJokersTurn(false);
        }
    }

    /**
     * Tells the entire joke, based on the current joke number.
     */
    @Override
    public void tellJoke()
    {
        this.tellJokeLine(KnockKnock.getJokeStart(), true);
        this.tellJokeLine(this.getSetup(), true);
        this.tellJokeLine(this.getPunchline(), false);
    }
}
