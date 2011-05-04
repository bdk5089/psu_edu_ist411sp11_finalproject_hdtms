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

import java.net.*;
import java.io.*;
import java.util.*;

/** Import RMI classes **/
import java.rmi.Naming; 
import java.rmi.RemoteException; 
import java.rmi.RMISecurityManager; 
import java.rmi.server.UnicastRemoteObject; 

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
			String ODBCString = args[0];
			Server server = new Server(ODBCString);		
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
	public Server(String ODBCString) throws IOException, RemoteException {
		super();
		// Bind server to the name "TicketServer"
		System.out.println("*************************************************");
		System.out.println(" Help Desk Ticket Manager Server Started");
		System.out.println("   Server Started on IP Address: "+InetAddress.getLocalHost().getHostAddress());
		
		String serverBinding = "TicketServer";
		try { 
			// Bind this object instance to the serverBinding. 
			Naming.rebind(serverBinding, this); 
			System.out.println("   Server Bound in Registry: "+serverBinding); 
        }catch (Exception e) { 
			System.out.println("   Server Error ["+serverBinding+"]: " + e.getMessage()); 
            e.printStackTrace(); 
        } 
		// CONNECT to database
		db = new Database(ODBCString);
		
		System.out.println("*************************************************");
		
        System.out.println(" ");		
		System.out.println("**** GETTING ACTIVE TICKETS       ****");
		this.clientsLoggedOn = new ArrayList<String>();
		this.activeTickets = getActiveTickets();		
		//Display Active Tickets
		Iterator hashIterator = this.activeTickets.keySet().iterator();
        while(hashIterator.hasNext()) {
			Object hashIndex = hashIterator.next();
			Ticket ticket = this.activeTickets.get(hashIndex);
				System.out.println("Ticket :" +ticket.toString());
			ArrayList<TicketLogEntry> logs = ticket.getLogEntries();
			for (int j=0;j<logs.size();j++){
				System.out.println("   Log : "+logs.get(j));
			}			
        }

	}
	
	/**
	*	logon(String username) throws RemoteException
	*	@param username is the username of the individual logging on 
	*		to the application
	*
	*	@return returns true if user exists in the database, false if otherwise.
	*/		
	public User logon(String username) throws RemoteException {
		User user = db.getUserByLogon(username);
		if (user == null) {
			return null;
		} else if (!clientsLoggedOn.contains(username)) {
			clientsLoggedOn.add(username);
		}
		
		return user;
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
		boolean success = checkInTicket(t, true);
		return success;
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
		boolean success = checkOutTicket(username, t.getID());
		return success;
	}

	/**
	*	getActiveTickets ()
	*	@return returns a HashMap of all the active tickets int he database.
	*/	
	public HashMap<String, Ticket> getActiveTickets() throws RemoteException {
		HashMap<String, Ticket> rs = db.getActiveTickets();
		return rs;
	}
	
	/**
	*	getStatusCodes ()
	*	@return returns a ArrayList of all the status codes.
	*/	
	public ArrayList<StatusCode> getStatusCodes() throws RemoteException {
		ArrayList<StatusCode> rs = db.getStatusCodes();
		return rs;
	}

	/**
	*	getResolutionCodes ()
	*	@return returns a ArrayList of all the resolution codes.
	*/	
	public ArrayList<ResolutionCode> getResolutionCodes() throws RemoteException {
		ArrayList<ResolutionCode> rs = db.getResolutionCodes();
		return rs;
	}
}