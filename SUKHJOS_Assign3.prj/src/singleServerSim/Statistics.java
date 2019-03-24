/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */
package singleServerSim;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author joshs
 *
 */
public class Statistics{

	private HashMap<String, ArrayList<Integer>> waitingTimes, queueSizes; 
	private ArrayList<Integer> timeSpentList;
	
	public boolean addTimeSpent(Integer time) {
		return timeSpentList.add(time);
	}
	
	public boolean addWaitingTime(String key, Integer time) {
		if(this.waitingTimes.containsKey(key) == false)
			return false;
		return this.waitingTimes.get(key).add(time);
	}
	
	public boolean addQueueSize(String key, Integer time) {
		if(this.queueSizes.containsKey(key) == false)
			return false;
		return this.queueSizes.get(key).add(time);
	}
	
	/** Default Constructor.
	 *  Instantiates a statistics object with a list of ATM waiting times,
	 *  a list of Shopping waiting times, and a list of Checkout waiting times
	 */
	public Statistics() {
		super();
		String[] queueList = {"ATM","Shopping","Checkout"};
		waitingTimes = new HashMap<String, ArrayList<Integer>>();
		this.queueSizes = new HashMap<String, ArrayList<Integer>>();
		for(String item : queueList) {
			waitingTimes.put(item, new ArrayList<Integer>());
			queueSizes.put(item, new ArrayList<Integer>());
		}
		timeSpentList = new ArrayList<Integer>();
		
	}

	/** Get the average value of an ArrayList of Integers
	 * 
	 * @param list the ArrayList to get the average of
	 * @return the average value of the arrayList
	 */
	private double getAverage(ArrayList<Integer> list) {
		if(list.size() == 0)
			return 0;
		double total = (double) list.stream().reduce(0, Integer::sum);
		double size = (double) list.size();
		return total / size;
	}
	
	/** Get the average wait time of entire visit to the fish market
	 * 
	 * @return the average time spent waiting whilst at the fish mark
	 */
	public double getAverageTimeSpent() {
		return getAverage(timeSpentList);
	}
	
	/** Get the average wait time during a given phase of the simulation
	 * 
	 * @param phase the simulation phase from which to get the average time
	 *  spent in queue
	 * @return the average time spent waiting in queue at the specified phase
	 * @throws Exception 
	 */
	public double getAverageWaitTime(String phase) throws RuntimeException {
		if(this.waitingTimes.containsKey(phase) == false)
			throw new RuntimeException();
		return getAverage( waitingTimes.get(phase) );
	}
	/** Get the average queue size during a given phase of the simulation
	 * 
	 * @param phase the simulation phase from which to get the average time
	 *  spent in queue
	 * @return the average time spent waiting in queue at the specified phase
	 * @throws Exception 
	 */
	public double getAverageQueueSize(String phase) throws RuntimeException {
		if(this.queueSizes.containsKey(phase) == false)
			throw new RuntimeException();
		return getAverage( queueSizes.get(phase) );
	}
	
}
