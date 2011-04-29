/*
	Filename: TicketServer.Java
	Classname: TicketServer
	Comments: The remote interface for our Ticketing system
*/

/** The TicketServer interface provides the remote interface for our ticketing system.
*	@author Eric So
*	@version 1.0
*/

import java.rmi.Remote;
import java.rmi.RemoteException; 
import java.util.*;

public interface TicketServer extends Remote {

	public boolean logon(String username) throws RemoteException;
	
	public boolean logoff(String username) throws RemoteException;
	
	public boolean updateTicket(String username, Ticket ticket) throws RemoteException;
	
	public HashMap<String, Ticket> getActiveTickets() throws RemoteException;

}