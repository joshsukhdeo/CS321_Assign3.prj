/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */
package singleServerSim;

import java.util.PriorityQueue;

/**
 * @author joshs
 *
 */
public class FishMarket {

private double INTERARRIVAL_TIME;
private double ATM_AVG_SERVICE_TIME;
private double AVG_CHECKOUT_TIME_PER_FISH;

private int clock = 0;
private PriorityQueue<Event> eventQueue;
private PriorityQueue<Client>clientQueue;
private Client serverATM, serverShopping, serverCheckout; 

public int getClock() {
	return clock;
}

public void setClock(int clock) {
	this.clock = clock;
}


/** Creates a FishMarket instance with the specified constants
 * @param iNTERARRIVAL_TIME The time between clients arriving to the fish market
 * @param aTM_AVG_SERVICE_TIME The average time a client spends with an ATM server
 * @param aVG_CHECKOUT_TIME_PER_FISH the average time a client spends 
 * with a Checkout server
 */
public FishMarket(double iNTERARRIVAL_TIME,
				  double aTM_AVG_SERVICE_TIME,
				  double aVG_CHECKOUT_TIME_PER_FISH )
{
	super();
	INTERARRIVAL_TIME = iNTERARRIVAL_TIME;
	ATM_AVG_SERVICE_TIME = aTM_AVG_SERVICE_TIME;
	AVG_CHECKOUT_TIME_PER_FISH = aVG_CHECKOUT_TIME_PER_FISH;
}

/** Runs the fish market simulation for the specified amount of time (in seconds)
 * 
 * @param simulationTime the number of seconds to run the simulation for
 */
public void run(int simulationTime) {
	// TODO Auto-generated body stub
	
}



}
