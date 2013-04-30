package tma03q1;

/**
 * Joker class.
 * Responsible for telling knock-knock jokes
 * @author Edward Wilde
 * Created on 26 April 2013
 */
public class Joker extends Thread
{
    private int jokeNum = 0; //the current joke

    protected final static String NAME = "Joker";
    
    private final static KnockKnock[] JOKES =
    {
        new KnockKnock("Mabel ", "Mabel is ringing."),
        new KnockKnock("Wendy ", "Wendy cows come home."),
        new KnockKnock("Stoat ", "Stoatily different person."),
        new KnockKnock("Dee ", "Delighted to meet you."),
    };

    /**
     * @return the current joke
     */
    public KnockKnock getCurrentJoke()
    {
        return JOKES[this.jokeNum];
    }

    /**
     * @return the total number of jokes.
     */
    public int getNumOfJokes()
    {
        return JOKES.length;
    }

    /**
     * @return the current joke's setup line as
     * given by the KnockKnock class
     */
    public String getSetup()
    {
        return this.getCurrentJoke().getSetup();
    }

    /*
     * @return the current joke's punchline as
     * given by the KnockKnock class
     */
    public String getPunchline()
    {
        return this.getCurrentJoke().getPunchline();
    }
    
    /*
     * Tells the joke for the current joke.
     *
     * @see #getCurrentJoke()
     */
    public void tellJoke()
    {
        System.out.printf("%-10s%s\n", NAME, KnockKnock.getJokeStart());
        KnockKnock.pause(400);
        System.out.printf("%-10s%s\n", NAME, this.getSetup());
        KnockKnock.pause(400);
        System.out.printf("%-10s%s\n", NAME, this.getPunchline());

    }

    /*
     * Moves on to the next joke.
     */
    private void nextJoke()
    {
        this.jokeNum = this.jokeNum + 1;
    }

    /*
     * Called when a thread starts executing this class.
     */
    @Override
    public void run()
    {
        for (KnockKnock knockKnock : JOKES)
        {
            this.tellJoke();
            this.nextJoke();
        }
    }
}
