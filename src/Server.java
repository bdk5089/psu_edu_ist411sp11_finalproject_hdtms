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

	/**
	*	main(String[] args)
	*	@param args is a String array of command-line values used to 
	*		instantiate the program....requires 1 command-line value
	* 		which is the ODBC name of the Access Database as defined
	*		in the ODBC manager of the local machine.
	*/
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
	
	/**
	*	Server(String ODBCString)
	*	@param ODBCString is a reference to an ODBC connection in the local machine
	*		ODBC manager.
	*/	
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
		activeTickets = getActiveTickets();
		
	}
	
	/**
	*	logon(String username) throws RemoteException
	*	@param username is the username of the individual logging on 
	*		to the application
	*
	*	@return returns true if user exists in the database, false if otherwise.
	*/		
	public boolean logon(String username) throws RemoteException {
		User user = db.getUserByLogon(username);
		if (user == null) {
			return false;
		} else if (!clientsLoggedOn.contains(username)) {
			clientsLoggedOn.add(username);
		}
		
		return true;
	}
	
	/**
	*	logoff(String username) throws RemoteException
	*	@param username is the username of the individual logging off of 
	*		the application
	*
	*	@return returns true if user is successfully removed from
	*		list of individuals logged on, false if otherwise.
	*/	
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
	*
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
	
	/**
	*	checkInTicket (Ticket t) always sets the checked out status in the database to null
	*	@param t is the Ticket object being updated
	*	@throws RemoteException
	*
	*	@return returns a boolean indicating successfull ticket update (true = success)
	*/		
	public boolean checkInTicket(Ticket t) throws RemoteException{
		return checkInTicket(t, true);
	}
	
	/**
	*	checkInTicket (Ticket t, boolean flag)
	*	@param t is the Ticket object being updated
	*	@param flag is the boolean flag used to indicate that the checked out status in the 
	*		database should be updated to null.  If flag is false, the ticket is updated in the
			database, but the checked out status is not cleared.
	*	@throws RemoteException
	*
	*	@return returns a boolean indicating successfull ticket checkout (true = success)
	*/	
	public boolean checkInTicket(Ticket t, boolean flag) throws RemoteException{
		boolean success = true;
		try{
			success = db.updateTicket(t);
			if (flag) {
				boolean response = db.checkInTicket(t);
			}
		}catch(Exception e){
			success = false;
		}
		return success;			
	}
	
	/**
	*	checkOutTicket (String username, int id)
	*	@param username is the logon name of the person who is checking out a ticket.
	*	@param id is the ID of the ticket being checked out.
	*	@throws RemoteException
	*
	*	@return returns a boolean indicating successfull ticket checkin (true = success)
	*/		
	public boolean checkOutTicket(String username, int id) throws RemoteException{
		boolean success = true;
		try{
			User user = db.getUserByLogon(username);
			success = db.checkOutTicket(id, user);
		}catch(Exception e){
			success = false;
		}
		return success;		
	}
	
	/**
	*	checkOutTicket (String username, int t)
	*	@param username is the logon name of the person who is checking out a ticket.
	*	@param t is the ticket being checked out
	*	@throws RemoteException
	*
	*	@return returns a boolean indicating successfull ticket checkin (true = success)
	*/		
	public boolean checkOutTicket(String username, Ticket t) throws RemoteException{
		return checkOutTicket(username, t.getID());
	}

	/**
	*	getActiveTickets ()
	*	@return returns a HashMap of all the active tickets int he database.
	*/	
	public HashMap<String, Ticket> getActiveTickets() throws RemoteException {
		ArrayList<Integer> s = new ArrayList<Integer>(2);
		s.add(1);
		s.add(2);
		s.add(10);
		HashMap<String, Ticket> activeTickets = db.getTicketsByStatus(s);
		return activeTickets;
	}
	
	/**
	*	getStatusCodes ()
	*	@return returns a ArrayList of all the status codes.
	*/	
	public ArrayList<StatusCode> getStatusCodes() throws RemoteException {
		return db.getStatusCodes();
	}

	/**
	*	getResolutionCodes ()
	*	@return returns a ArrayList of all the resolution codes.
	*/	
	public ArrayList<ResolutionCode> getResolutionCodes() throws RemoteException {
		return db.getResolutionCodes();
	}
}