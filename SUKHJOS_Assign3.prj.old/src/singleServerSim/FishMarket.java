/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */
package singleServerSim;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author joshs
 *
 */
public class FishMarket {

	public class Market_Arrival extends Event {
		
		public final static String TYPE = "Market";
		
		/** Schedule an arrival to the market at a randomly generated time
		 * @param time The time to schedule an arrival for
		 */
		public Market_Arrival() {
			super(clock + (int) RandBox.expo(INTERARRIVAL_TIME));
		}
		
		/** Schedule an arrival to the market at a specified time
		 * @param time
		 */
		public Market_Arrival(int time) {
			super(time);
		}
		

		protected int generateScheduledTime() {
			int serviceTime = (int) RandBox.expo(INTERARRIVAL_TIME);
			return clock + serviceTime;
		}	

		/* (non-Javadoc)
		 * @see singleServerSim.Event#process()
		 */
		@Override
		public void process() {
			clock = time;
			Client newClient = new Client(clock);
			scheduleNextEvent(newClient);
			
			Event_Queue.add( new Market_Arrival() );
		}

		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#generateScheduledTime(singleServerSim.Client)
		 */
		@Override
		protected int generateScheduledTime(Client client) {
			return generateScheduledTime();
		}

		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			if(client.getMoney() < 20) {
				if(ATM_Server != null) {
					ATM_Queue.add(client);
				}
				else {
					Event_Queue.add(new ATM_Departure(client));
				}
			}
			else {
				if(Shopping_Server != null)
					Shopping_Queue.add(client);
				else
					Event_Queue.add( new Shopping_Departure(client) );
			}
		}


	}
	
	public class ATM_Departure extends Event {
		
		public final static String TYPE = "ATM";

		
		/** Schedule an ATM departure at specified time
		 * @param time
		 */
		public ATM_Departure(int time) {
			super(time);
		}
		
		/** Schedule an ATM departure at specified time
		 * @param time
		 */
		public ATM_Departure(Client client) {
			super(client);
			ATM_Server = client;
		}
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#generateScheduledTime()
		 */
		@Override
		public int generateScheduledTime(Client client) {
			int serviceTime = (int) Event.RandBox.expo(ATM_AVG_SERVICE_TIME);
			return clock + serviceTime;
		}
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#process()
		 */
		@Override
		public void process() {
			Client currentClient = ATM_Server;
			int moneyWithdrawn = (int) Math.random() * 31 + 10;
			currentClient.addMoney(moneyWithdrawn);
			scheduleNextEvent(currentClient);
			
			if(!ATM_Queue.isEmpty()) {
				Client nextClient = ATM_Queue.poll();

				Event_Queue.add( new ATM_Departure(nextClient) );
				
				Integer queueWaitingTime = clock - ATM_Server.getPhaseArrivalTime();
				stats.addWaitingTime("ATM", queueWaitingTime);
				stats.addWaitingTime("ATM_QueueSize", ATM_Queue.size());
			}
		}

		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			client.setPhaseArrivalTime(clock);
			ATM_Server = null;
			if(Shopping_Server == null) {
				Shopping_Server = client;
				Event_Queue.add( new Shopping_Departure(client) );
			} else {
				Shopping_Queue.add(client);
			}
		}
	

	}
	public class Shopping_Departure extends Event {
		
		public final static String TYPE = "Shopping";
		
		/** Schedule an Shopping departure at a specified time
		 * @param time
		 */
		public Shopping_Departure(int time) {
			super(time);
		}

		/** Schedule an Shopping departure at a specified time
		 * @param time
		 */
		public Shopping_Departure(Client client) {
			super(client);
			setServer(client;
		}
		
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#generateScheduledTime()
		 */
		@Override
		public int generateScheduledTime(Client client) {
			int fishToAcquire =  (int) (client.getMoney() % PRICE_PER_FISH);
			int serviceTime = (int) Event.RandBox.expo(fishToAcquire * AVG_CHECKOUT_TIME_PER_FISH);
			return clock + serviceTime;
		}	
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#process()
		 */
		@Override
		public void process() {
			Client currentClient = Shopping_Server;
			int fishAddedToCart =  (int) (currentClient.getMoney() % PRICE_PER_FISH);
			currentClient.setFish(fishAddedToCart);
			scheduleNextEvent(currentClient);
			
			if(!Shopping_Queue.isEmpty()) {
				Client nextClient = Shopping_Queue.poll();
				Event_Queue.add( new Shopping_Departure(nextClient) );
				
				// Handle stats for Shopping queue time
				Integer queueWaitingTime = clock - nextClient.getPhaseArrivalTime();
				stats.addWaitingTime("Shopping", queueWaitingTime);
				stats.addWaitingTime("Shopping_QueueSize", Shopping_Queue.size());
			}
		}

		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			Shopping_Server = null;
			client.setPhaseArrivalTime(clock);
			
			if(Checkout_Server == null) {
				setServer(TYPE, null);
				Event_Queue.add( new Checkout_Departure(client) );
			} else {
				Checkout_Queue.add(client);
			}
			
		}
		

	}
	public class Checkout_Departure extends Event {

		public final static String TYPE = "Checkout";
		
		/** Schedule an Checkout departure at a specified time
		 * @param time The time to set
		 */
		public Checkout_Departure(int time) {
			super(time);
		}
		
		/** Schedule an Checkout departure for a given client
		 * @param client The client to schedule the departure for
		 */
		public Checkout_Departure(Client client) {
			super(client);
			setServer(TYPE, client);
		}
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#generateScheduledTime()
		 */
		public int generateScheduledTime(Client client){
			int serviceTime = (int) Event.RandBox.expo(client.getFish() * AVG_CHECKOUT_TIME_PER_FISH);
			return clock + serviceTime;
		}	
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#process()
		 */
		@Override
		public void process() {
			Client currentClient = getServer(TYPE);
			int totalPrice =  (int) (currentClient.getFish() % PRICE_PER_FISH);
			currentClient.removeMoney(totalPrice);
			
			scheduleNextEvent(currentClient);
			
			if(!Checkout_Queue.isEmpty()) {
				Client nextClient = Checkout_Queue.poll();
				Event_Queue.add( new Checkout_Departure(nextClient) );
				
				// Handle Stats for the Checkout queue
				Integer queueWaitingTime = clock - nextClient.getPhaseArrivalTime();
				
				stats.addWaitingTime("Checkout", queueWaitingTime);
				stats.addWaitingTime("Checkout_QueueSize", Checkout_Queue.size());
			}
		}


		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			setServer(TYPE, null);
			client.setPhaseArrivalTime(clock);
			
			// Handle stats for time spent in the market when a client leaves
			Integer timeSpentInMarket = clock - client.getArrivalTime();
			stats.addWaitingTime("Market", timeSpentInMarket);
		}	

	}
	
//private final static String TYPE = "FishMarket";
private double INTERARRIVAL_TIME;
private double ATM_AVG_SERVICE_TIME;
private double AVG_CHECKOUT_TIME_PER_FISH;
private double PRICE_PER_FISH = 10;

private int clock = 0;
private Statistics stats;
private PriorityQueue<Event> Event_Queue;
private ConcurrentLinkedQueue<Client>ATM_Queue, Shopping_Queue, Checkout_Queue;
private Client ATM_Server, Shopping_Server, Checkout_Server;
private HashMap<String, Client> server; 

private boolean isServerFree(String type) {
	return (server.get(type) == null);
}

private Client getServer(String type) {
	System.out.println(type);
	return server.get(type);
}

private Client setServer(String type, Client client) {
	return server.put(type,client);
}

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
	Event_Queue = new PriorityQueue<Event>();
	server = new HashMap<String,Client>();
	server.put("ATM", null);
	server.put("Checkout", null);
	server.put("Shopping", null);
	ATM_Queue = new ConcurrentLinkedQueue<Client>();
	Shopping_Queue = new ConcurrentLinkedQueue<Client>();
	Checkout_Queue = new ConcurrentLinkedQueue<Client>();
	stats = new Statistics();
}

/** Runs the fish market simulation for the specified amount of time (in seconds)
 * 
 * @param simulationTime the number of seconds to run the simulation for
 */
public void run(int simulationTime) {
	Event_Queue.add( new Market_Arrival() );
	while(clock < simulationTime) {
		//if(clock < 1000)
			//System.out.println("Clk: "+clock+" EventQueue: "+Event_Queue.toString());
		Event_Queue.poll().process();
	}

	System.out.println("ATM Queue Average Wait Time: " + stats.getAverageWaitTime("ATM"));
	System.out.println("Shopping Queue Average Wait Time: " + stats.getAverageWaitTime("Shopping"));
	System.out.println("Checkout Queue Average Wait Time: " + stats.getAverageWaitTime("Checkout"));
	System.out.println("ATM Queue Average Size: " + stats.getAverageWaitTime("ATM_QueueSize"));
	System.out.println("Shopping Queue Average Size: " + stats.getAverageWaitTime("Shopping_QueueSize"));
	System.out.println("Checkout Queue Average Size: " + stats.getAverageWaitTime("Checkout_QueueSize"));

	System.out.println("Average time spent in market : " + stats.getAverageWaitTime("Market"));
}


}
