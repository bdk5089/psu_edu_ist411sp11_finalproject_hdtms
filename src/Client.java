/*
	Filename: Client.Java
	Classname: Client
	Comments: The RMI client for our Ticketing system
*/

/** The Client class connects to the server via an RMI object. It handles interaction between
*	the user and the server, with interactions consisting of retriving active tickets and updating
*	tickets.
*	@author Eric So
*	@version 1.0
*/

import java.rmi.Naming; 
import java.rmi.RemoteException; 


public class Client {

	private String username;
	private ArrayList<Ticket> activeTickets;
	
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
			ticketServerObject.logon(this.username);
			
			// Get the list of active tickets
			this.activeTickets = ticketServerObject.getActiveTickets();
			
			
		} catch (Exception e) {
			// Exception handling code
		}
		
	}
	
	public static void main(String[] args) {
		// Args: username
		Client client = new Client(args[0]);
	}


}