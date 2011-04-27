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


public interface TicketServer extends Remote {

	public void logon(String username) throws RemoteException;
	
	public void logoff(String username) throws RemoteException;
	
	public void updateTicket(String username, Ticket ticket) throws RemoteException;
	
	public ArrayList<Ticket> getActiveTickets() throws RemoteException;

}