/*
	Filename: Server.Java
	Classname: Server
	Comments: The RMI Server for our Ticketing system
*/

/** The Server class handles client connections and communicates with the database. Clients
*	connect to the server via an RMI object.
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server extends UnicastRemoteObject implements TicketServer {

	public static void main(String[] args) {
		
		try {
			// Try to create a Server object
			Server server = new Server(args[0]);
			
			// Bind server to the name "TicketServer"
			Naming.rebind("TicketServer", server);
			System.out.println("TicketServer is bound in registry");
			
			// TODO security manager?
			
		} catch (Exception e) {
			System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
		}
	}
	
	private ArrayList<String> clientsLoggedOn;
	private HashMap<String, Ticket> activeTickets;
	private Database db;
	
	public Server(String ODBCString) throws RemoteException {
		super();
		
		System.out.println("*************************************************");
		System.out.println("**** Help Desk Ticket Manager Server Started ****");
		System.out.println("*************************************************");
		
		// CONNECT to database
		Database db = new Database(ODBCString);
			
		this.clientsLoggedOn = new ArrayList<String>();
		
		// TODO probably makes better sense to recover these from the Database class
		// Or we could write a method to manually add each to the ArrayList
		this.activeTickets = new HashMap<String, Ticket>();
		
		// Get the active tickets from the database
		activeTickets.
		
	}
	
	public boolean logon(String username) throws RemoteException {
		User user = db.getUserByLogon(username);
		if (user == null) {
			return false;
		} else if (!clientsLoggedOn.contains(username)) {
			clientsLoggedOn.add(username);
		}
		
		return true;
	}
	
	public boolean logoff(String username) throws RemoteException {
		if (clientsLoggedOn.contains(username)) {
			clientsLoggedOn.remove(username);
		}
		return true;
	}
	
	/**
	*	updateTicket (String username, Ticket ticket)
	*	@param username is the username of the client
	*	@param ticket is the Ticket object being updated
	*	@throws RemoteException
	*	@return returns a boolean indicating successfull ticket update (true = success)
	*/	
	public boolean updateTicket(String username, Ticket ticket) throws RemoteException {
		// TODO get changes done to ticket and send to database
		
		// Check to see if the communicating client is logged on
		boolean loggedOn = clientsLoggedOn.contains(username);
		
		if (loggedOn) {
			// do stuff
			
			return true;
		} else {
			// User is not logged on
			
			return false;
		}
	}
	
	public HashMap<String, Ticket> getActiveTickets() throws RemoteException {
		return activeTickets;
	}
	
	



}