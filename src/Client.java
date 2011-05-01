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
import java.awt.*;
import javax.swing.*;


public class Client {
	
	/**
	*	main(String[] args)
	*	@param args is a String array of command-line values used to 
	*		instantiate the program....requires 1 command-line value
	* 		which is the username of the person using the client.
	*/
	public static void main(String[] args) {
		// Create the Client
		Client client = new Client(args[0]);
	}
	

	private String username;
	private HashMap<String, Ticket> activeTickets;
	private JFrame activeTicketsFrame;
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
			this.activeTicketsFrame = new ClientActiveTicketsFrame(username, activeTickets);
			this.activeTicketsFrame.setSize(200, 600);
			this.activeTicketsFrame.setVisible(true);	
		} catch (Exception e) {
			// Exception handling code
			System.out.println("Error: An exception has occured.");
			System.out.println("Exception message: " + e.getMessage());
		}
		
	}
}