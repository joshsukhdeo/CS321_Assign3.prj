/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */
package singleServerSim;

/**
 * @author joshs
 *
 */
public class Client {
	private int arrivalTime;
	private int money;
	private int id;
	private static int currentId = 1;
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	/** Create a client object with their time of arrival and an amount
	 *  of money between 1-100 dollars
	 * @param arrivalTime the time wherein the client arrived to the market
	 */
	public Client(int arrivalTime) {
		super();
		this.arrivalTime = arrivalTime;
		this.money = (int) (100 * Math.random() + 1);
		this.id = currentId;
		currentId++;
	}
	
	
}
