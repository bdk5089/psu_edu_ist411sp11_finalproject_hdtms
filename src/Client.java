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
import java.net.*;


public class Client {
	
	/**
	*	main(String[] args)
	*	@param args is a String array of command-line values used to 
	*		instantiate the program....requires 1 command-line value
	* 		which is the username of the person using the client.
	*/
	public static void main(String[] args) {
		// Create the Client
		Client client = new Client(args[0], args[1]);
	}
	

	private String username;
	private HashMap<String, Ticket> activeTickets;
	private JFrame activeTicketsFrame;
	static TicketServer ticketServerObject = null;
	
	/**
	*	Client(String IPAddress, String username)
	*	@param IPAddress is the IP address of the local machine.
	*	@param username is the username of the user instantiating the client.
	*/
	public Client(String IPAddress, String username) {
		System.out.println("*************************************************");
		System.out.println(" Help Desk Ticket Manager Client Started");
		
		this.username = username;
		
		try {
			// Get the RMI object
			if (IPAddress.toLowerCase().equals("localhost")){
				IPAddress = InetAddress.getLocalHost().getHostAddress();
			}
			System.out.println("   Connecting To Server: "+IPAddress);
			ticketServerObject = (TicketServer) Naming.lookup("//" + IPAddress + "/TicketServer");
			
			// Logon to the server
			boolean loggedOn = false;
			System.out.println("   Logging On: "+this.username);
			loggedOn = ticketServerObject.logon(this.username);
			
			// Check to see if the user was able to log on. If not, exit.
			if (!loggedOn) {
				System.out.println("   Error: Username " + this.username + " not in database.");
				System.exit(-1);
			}
			
			// TODO The activeTickets HashMap needs to be continously updated. We could spawn a thread that will do this
			
			// Get the list of active tickets
			this.activeTickets = ticketServerObject.getActiveTickets();
			
			// Ininialize the GUI
			this.activeTicketsFrame = new ClientActiveTicketsFrame(username, activeTickets, ticketServerObject);
			this.activeTicketsFrame.setSize(200, 600);
			this.activeTicketsFrame.setVisible(true);	
		} catch (Exception e) {
			// Exception handling code
			System.out.println("  Error Message: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("*************************************************");
	}
}