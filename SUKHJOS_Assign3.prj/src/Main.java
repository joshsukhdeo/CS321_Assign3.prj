import singleServerSim.FishMarket;

/**
 * Name: Joshua Sukhdeo
 * Student ID: 002217503
 */

/**
 * @author joshs
 *
 */
public class Main {
	public static void main(String[] args) {
 	    FishMarket F = new FishMarket (120.0 , 30.0, 30.0);
        F.run(2 * 30 * 24 * 60 * 60); // seconds in two months
      
      } 
}
