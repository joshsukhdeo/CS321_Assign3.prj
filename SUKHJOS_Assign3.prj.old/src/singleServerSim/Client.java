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
	private int fish;
	private int phaseArrivalTime;
	private int id;
	private static int currentId = 1;
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getPhaseArrivalTime() {
		return phaseArrivalTime;
	}

	public void setPhaseArrivalTime(int phaseArrivalTime) {
		this.phaseArrivalTime = phaseArrivalTime;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void addMoney(int money) {
		this.money += money;
	}
	
	public void removeMoney(int money) {
		this.money += money;
	}
	
	public int getFish() {
		return fish;
	}

	public void setFish(int fish) {
		this.fish = fish;
	}

	
	/** Create a client object with their time of arrival and an amount
	 *  of money between 0-40 dollars
	 * @param arrivalTime the time wherein the client arrived to the market
	 */
	public Client(int arrivalTime) {
		super();
		this.arrivalTime = arrivalTime;
		this.setPhaseArrivalTime(arrivalTime);
		this.money = (int) (41 * Math.random());
		this.fish = 0;
		this.id = currentId;
		currentId++;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client [arrivalTime=" + arrivalTime + ", money=" + money + ", fish=" + fish + ", id=" + id + "]";
	}

	
}
