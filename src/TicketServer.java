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
import java.net.*;
import java.io.*;
import java.util.*;

public interface TicketServer extends Remote {

	public User logon(String username) throws RemoteException;
	
	public boolean logoff(String username) throws RemoteException;
	
	public boolean checkOutTicket(String username, int id) throws RemoteException;
	public boolean checkOutTicket(String username, Ticket t) throws RemoteException;
	
	public boolean checkInTicket(Ticket ticket) throws RemoteException;
	public boolean checkInTicket(Ticket ticket, boolean flag) throws RemoteException;
	
	public HashMap<String, Ticket> getActiveTickets() throws RemoteException;
	
	public ArrayList<StatusCode> getStatusCodes() throws RemoteException;
	public ArrayList<ResolutionCode> getResolutionCodes() throws RemoteException;
}