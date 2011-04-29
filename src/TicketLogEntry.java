import java.util.*;

/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/
public class TicketLogEntry implements java.io.Serializable{
	
	private int id;
	private int ticketID;
	private String logEntry;
	private User performedBy;
	private Date performedDate;
	/**
	*	TicketLogEntry(int i, int r, String l, User u, Date d)
	*	@param i is the log entry id (assigned by database, 0 if not yet submitted to database);
	*	@param r is the ticket id reference;
	*	@param l is the log engry text;
	*	@param u is the User that made the log entry;
	*	@param d is the date the log entry was created;
	*/	
	public TicketLogEntry(int i, int r, String l, User u, Date d){
		id = i;
		ticketID = r;
		logEntry = l;
		performedBy = u;
		performedDate = d;
	}
	/**
	*	TicketLogEntry(int r, String l, User u, Date d)
	*	@param r is the ticket id reference;
	*	@param l is the log engry text;
	*	@param u is the User that made the log entry;
	*	@param d is the date the log entry was created;
	*/		
	public TicketLogEntry(int r, String l, User u, Date d){
		id = 0;
		ticketID = r;
		logEntry = l;
		performedBy = u;
		performedDate = d;
	}
	
	/**
	*	getID ()
	*	returns log ID value
	*/	
	public int getID(){
		return id;
	}
	/**
	*	setID ()
	*	sets the log ID value
	*/	
	public void setID(int i){
		id = i;
	}
	/**
	*	getTicketID ()
	*	returns ticket ID value
	*/	
	public int getTicketID(){
		return ticketID;
	}
	/**
	*	getEntry ()
	*	returns the entry
	*/
	public String getEntry(){
		return logEntry;
	}
	/**
	*	getPerformedBy ()
	*	returns the user who performed work
	*/
	public User getPerformedBy(){
		return performedBy;
	}	
	/**
	*	getPerformedDate ()
	*	returns the date of the work performed
	*/
	public Date getPerformedDate(){
		return performedDate;
	}	
	/**
	*	toString ()
	*	returns a string representation of the object  
	*/	
	public String toString(){
		return "["+performedDate+"] "+logEntry+"";
	}
}