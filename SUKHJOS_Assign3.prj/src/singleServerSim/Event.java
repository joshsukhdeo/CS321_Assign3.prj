package singleServerSim;
/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */

import java.util.Random;

/**
 * @author joshs
 *
 */
public abstract class Event implements Comparable<Event> {

	/**
		 * @author joshs
		 *
		 */
	static class RandBox extends Random {

		private static final long serialVersionUID = 8156284894755258434L;
		
		
		/** Generates a random value from an exponential distribution
		 * that is generated from the mean value supplied
		 * @param mean the mean value of the exponential distribution to generate
		 * @return a random value from the generated exponential distribution
		 */
		static double expo(double mean) {
			double x = Math.random();
			x = -mean * Math.log(x);
			return x;
		}


	}
	
	private int time;
	
	private Client client;

	public int getTime() {
		return time;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName()+" [time=" + this.time + ", client=" + this.client + "]";
	}

	public Event(int time) {
		this.time = time;
	}
	
	public Event(Client client) {
		this.time = generateScheduledTime(client);
		this.setClient(client);
	}
	
	/** Generates a time to schedule an Event for
	 * 
	 * @return the time to schedule an Event for
	 */
	protected abstract int generateScheduledTime(Client client);
	
	
	/** Process the event's actions 
	 * 
	 */
	protected abstract void scheduleNextEvent(Client client);
	/** Process the event's actions 
	 * 
	 */
	public abstract void process();
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	
	@Override
	public int compareTo(Event arg0) {
		if(this.time == arg0.time)
			return 0;
		return (this.time > arg0.time) ? 1 : -1;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(time);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time))
			return false;
		return true;
	}
	
}
