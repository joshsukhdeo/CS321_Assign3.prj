/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */
package singleServerSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * @author joshs
 *
 */
public class Statistics{

	private static final long serialVersionUID = -1447896051344243508L;
	
	private HashMap<String, ArrayList<Integer>> waitingTimes; 

	public boolean addWaitingTime(String key, Integer time) {
		if(this.waitingTimes.containsKey(key) == false)
			return false;
		return this.waitingTimes.get(key).add(time);
	}
	
	/** Default Constructor.
	 *  Instantiates a statistics object with a list of ATM waiting times,
	 *  a list of Shopping waiting times, and a list of Checkout waiting times
	 */
	public Statistics() {
		super();
		Map<String,Integer> mapBlueprint = new HashMap<String,Integer>();
		mapBlueprint.put("ATM", 1);
		mapBlueprint.put("Shopping", 1);
		mapBlueprint.put("Checkout", 1);
		mapBlueprint.put("ATM_QueueSize", 1);
		mapBlueprint.put("Shopping_QueueSize", 1);
		mapBlueprint.put("Checkout_QueueSize", 1);
		mapBlueprint.put("Market", 1);
		
		this.waitingTimes = initializeHashMap(mapBlueprint);

	}
	
	/** Initializes a HashMap<String,ArrayList<Integer>[]> from a 
	 *  a Map<String, Integer>, where the size of ArrayList<Integer>[]
	 *  is determined by the Map's int value. 
	 *  (Map<String, Integer> has pairs of (Key to set, ArraySize to set)
	 * 
	 * @param blueprint the map containing the keys and arraySize of the array
	 * @return a hashMap of string and arrayList<integers>
	 */
	private HashMap<String,ArrayList<Integer>> initializeHashMap(Map<String, Integer> blueprint) {
		HashMap<String,ArrayList<Integer>> hashMap = new HashMap<String,ArrayList<Integer>>();
		Iterator<Map.Entry<String,Integer>> it = blueprint.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Integer> pair = it.next();
			hashMap.put( pair.getKey(), new ArrayList<Integer>() );
		}
		return hashMap;
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
	public double getAverageWaitTime() {
		double avgTotalWaitTime = 0;
		
		for(ArrayList<Integer> arrayOfLists : waitingTimes.values())
			avgTotalWaitTime += getAverage(arrayOfLists);
		return avgTotalWaitTime;
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
	
	
}
