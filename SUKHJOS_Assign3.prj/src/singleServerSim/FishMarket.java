/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */
package singleServerSim;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;


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
			clock = getTime();
			Client newClient = new Client(clock);
			this.setClient(newClient);
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
				if(isServerFree("ATM")) {
					Event_Queue.add(new ATM_Departure(client));
				}
				else {
					getServerQueue("ATM").add(client);
				}
			}
			else {
				if(isServerFree("Shopping"))
					Event_Queue.add( new Shopping_Departure(client) );
				else
					getServerQueue("Shopping").add(client);
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
			assignToServer(TYPE, client);
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
			clock = getTime();
			Client currentClient = getClient();
			// Stats for the current client's queue experience
			Integer queueWaitingTime = clock - currentClient.getPhaseArrivalTime();
			stats.addWaitingTime(TYPE, queueWaitingTime);
			stats.addQueueSize(TYPE, getServerQueue(TYPE).size());
			
			int moneyWithdrawn = (int) Math.random() * 31 + 10;
			currentClient.addMoney(moneyWithdrawn);
			
			scheduleNextEvent(currentClient);
			
			if(!getServerQueue(TYPE).isEmpty()) {
				Client nextClient = getServerQueue(TYPE).poll();

				Event_Queue.add( new ATM_Departure(nextClient) );
				

			}
		}

		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			client.setPhaseArrivalTime(clock);
			removeFromServer(TYPE, client);
			if(isServerFree("Shopping")) {
				Event_Queue.add( new Shopping_Departure(client) );
			} else {
				getServerQueue("Shopping").add(client);
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
			assignToServer(TYPE, client);
		}
		
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#generateScheduledTime()
		 */
		@Override
		public int generateScheduledTime(Client client) {
			int fishToAcquire =  (int) (client.getMoney() / PRICE_PER_FISH);
			int serviceTime = (int) Event.RandBox.expo(fishToAcquire * AVG_CHECKOUT_TIME_PER_FISH);
			return clock + serviceTime;
		}	
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#process()
		 */
		@Override
		public void process() {
			clock = getTime();
			Client currentClient = getClient();
			int fishAddedToCart =  (int) (currentClient.getMoney() / PRICE_PER_FISH);
			currentClient.setFish(fishAddedToCart);
			
			// Stats for the current client's queue experience
			Integer queueWaitingTime = clock - currentClient.getPhaseArrivalTime();
			stats.addWaitingTime(TYPE, queueWaitingTime);
			stats.addQueueSize(TYPE, getServerQueue(TYPE).size());
			
			scheduleNextEvent(currentClient);
			
			if(!getServerQueue(TYPE).isEmpty()) {
				Client nextClient = getServerQueue(TYPE).poll();
				Event_Queue.add( new Shopping_Departure(nextClient) );
			}
		}

		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			removeFromServer(TYPE, client);
			client.setPhaseArrivalTime(clock);
			
			if(isServerFree("Checkout")) {
				Event_Queue.add( new Checkout_Departure(client) );
			} else {
				getServerQueue("Checkout").add(client);
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
			assignToServer(TYPE, client);
		}
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#generateScheduledTime()
		 */
		public int generateScheduledTime(Client client) {
			int serviceTime = (int) Event.RandBox.expo(client.getFish() * AVG_CHECKOUT_TIME_PER_FISH);
			return clock + serviceTime;
		}	
		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#process()
		 */
		@Override
		public void process() {
			clock = getTime();
			Client currentClient = getClient();
			int totalPrice =  (int) (currentClient.getFish() * PRICE_PER_FISH);
			currentClient.removeMoney(totalPrice);
			
			// Handle Stats for the Checkout queue
			Integer queueWaitingTime = clock - currentClient.getPhaseArrivalTime();
			
			stats.addWaitingTime(TYPE, queueWaitingTime);
			stats.addQueueSize(TYPE, getServerQueue(TYPE).size());
			scheduleNextEvent(currentClient);
			
			if(!getServerQueue(TYPE).isEmpty()) {
				Client nextClient = getServerQueue(TYPE).poll();
				Event_Queue.add( new Checkout_Departure(nextClient) );
				

			}
		}


		
		/* (non-Javadoc)
		 * @see singleServerSim.Event#scheduleNextEvent(singleServerSim.Client)
		 */
		@Override
		protected void scheduleNextEvent(Client client) {
			removeFromServer(TYPE, client);
			client.setPhaseArrivalTime(clock);
			
			// Handle stats for time spent in the market when a client leaves
			stats.addTimeSpent(clock - client.getArrivalTime());
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
private HashMap<String, ConcurrentLinkedQueue<Client>> serverQueue;
private HashMap<String, Client> server; 


private boolean isServerFree(String type) {
	return (server.get(type) == null);
}



private Client assignToServer(String type, Client client) {
	return server.put(type,client);
}

private Client removeFromServer(String type, Client client) {
	return server.put(type,null);
}

private ConcurrentLinkedQueue<Client> getServerQueue(String type) {
	return serverQueue.get(type);
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
	server.put("Shopping", null);
	server.put("Checkout", null);
	serverQueue = new HashMap<String, ConcurrentLinkedQueue<Client>>();
	serverQueue.put("ATM", new ConcurrentLinkedQueue<Client>());
	serverQueue.put("Shopping", new ConcurrentLinkedQueue<Client>());
	serverQueue.put("Checkout", new ConcurrentLinkedQueue<Client>());
	stats = new Statistics();
}

/** Runs the fish market simulation for the specified amount of time (in seconds)
 * 
 * @param simulationTime the number of seconds to run the simulation for
 */
public void run(int simulationTime) {
	Event_Queue.add( new Market_Arrival() );
	while(clock < simulationTime) {
		Event_Queue.poll().process();

	}

	System.out.println("ATM Queue Average Wait Time: " + stats.getAverageWaitTime("ATM")/60);
	System.out.println("Shopping Queue Average Wait Time: " + stats.getAverageWaitTime("Shopping")/60);
	System.out.println("Checkout Queue Average Wait Time: " + stats.getAverageWaitTime("Checkout")/60);
	System.out.println("ATM Queue Average Size: " + stats.getAverageQueueSize("ATM"));
	System.out.println("Shopping Queue Average Size: " + stats.getAverageQueueSize("Shopping"));
	System.out.println("Checkout Queue Average Size: " + stats.getAverageQueueSize("Checkout"));
	System.out.println();
	System.out.println("Average time spent in market : " + stats.getAverageTimeSpent()/60);
}


}
