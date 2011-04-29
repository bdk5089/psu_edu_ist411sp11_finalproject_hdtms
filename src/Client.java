/*
	Filename: Client.java
	Classname: Client
	Comments: The RMI client for our Ticketing system
*/

/** The Client class connects to the server via an RMI object. It handles interaction between
*	the user and the server, with interactions consisting of retriving active tickets and updating
*	tickets.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.rmi.Naming; 
import java.rmi.RemoteException; 
import java.util.*;

public class Client {

	private String username;
//	private ArrayList<Ticket> activeTickets;
	private HashMap<String, Ticket> activeTickets;
	
	// TODO May need to create a HashMap of the activeTickets b/c we need a way to look them up
	
	
	static TicketServer ticketServerObject = null;

	public Client(String username) {
		System.out.println("*************************************************");
		System.out.println("**** Help Desk Ticket Manager Client Started ****");
		System.out.println("*************************************************");
		
		this.username = username;
		
		try {
			// Get the RMI object
			ticketServerObject = (TicketServer) Naming.lookup("//" + "localhost" + "/TicketServer");
			
			
			// Logon to the server
			boolean loggedOn = false;
			loggedOn = ticketServerObject.logon(this.username);
			
			// Check to see if the user was able to log on. If not, exit.
			if (!loggedOn) {
				System.out.println("Error: Username " + this.username + " not in database.");
				System.exit(-1);
			}
			
			// Get the list of active tickets
			this.activeTickets = ticketServerObject.getActiveTickets();
			
			// Ininialize the GUI
			
			
		} catch (Exception e) {
			// Exception handling code
		}
		
	}
	
	public static void main(String[] args) {
		// Args: username
		Client client = new Client(args[0]);
	}


}