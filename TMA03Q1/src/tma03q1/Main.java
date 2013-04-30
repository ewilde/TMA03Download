package tma03q1;

/**
 *
 * @author Edward Wilde
 * Created on 26th March 2013
 */
public class Main
{
    public static void main(String[] args)
    {
        Joker1 joker = new Joker1();
        joker.start();

        Listener1 listener = new Listener1(joker);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();        
    }
}