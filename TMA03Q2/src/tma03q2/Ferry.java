/*
 * Ferry.java
 *
 */

package tma03q2;

import java.util.ArrayList;
import java.util.List;

import utilities.Direction;

import static utilities.Delays.*;
import static utilities.Direction.*;

/**
 *
 * @author M257
 */
public class Ferry
{
    private Direction direction;
    
    private List<Passenger> passengerList;
    
    
    /** Creates a new instance of Ferry, initially eastbound */
    public Ferry()
    {
        direction = EASTBOUND;
        passengerList = new ArrayList<Passenger>();
    }
    
    public synchronized void board(Passenger passenger) throws InterruptedException
    {
        while(this.getDirection() != passenger.getDirection() ||
              this.isFerryFull())
        {
            System.out.println("Passenger " + passenger.getPassengerID()
                + " waiting at " + passenger.getDirection());
            this.wait();
        }

        this.AddPassenger(passenger);
    }
    
    private synchronized void AddPassenger(Passenger passenger)
    {
        System.out.println("Passenger " + passenger.getPassengerID()
        + " " + passenger.getDirection() + " boards");
        this.passengerList.add(passenger);
    }
    
    private synchronized boolean isFerryFull()
    {
        return this.passengerList.size() >= 2;
    }

    public synchronized void cross()
    {
        System.out.println("Ferry crossing " + getDirection() + "...");
        System.out.println("Passengers on board " + this.passengerList.size());
        delay(2000);
        System.out.println("Ferry arrives at opposite bank");
        for (Passenger eachTraveller : passengerList)
        {
            System.out.println("Passenger " + eachTraveller.getPassengerID() + " alights");
        }
        passengerList.clear();
        swapDirection();

        this.notifyAll();
    }
    
    public synchronized Direction getDirection()
    {
        return direction;
    }
    
    public synchronized void setDirection(Direction aDirection)
    {
        direction = aDirection;
    }
    
    private void swapDirection()
    {
        if (getDirection() == EASTBOUND)
        {
            setDirection(WESTBOUND);
        }
        else
        {
            setDirection(EASTBOUND);
        }
    }
    
}
