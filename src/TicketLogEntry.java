import java.util.*;

/** The TicketLogEntry class used to store log entry information 
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
	*   @return returns log ID value
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
	*   @return returns ticket ID value
	*/	
	public int getTicketID(){
		return ticketID;
	}
	/**
	*	setTicketID ()
	*   @return returns ticket ID value
	*/	
	public void setTicketID(int i){
		ticketID = i;
	}
	/**
	*	getEntry ()
	*   @return returns the entry
	*/
	public String getEntry(){
		return logEntry;
	}
	/**
	*	getPerformedBy ()
	*   @return returns the user who performed work
	*/
	public User getPerformedBy(){
		return performedBy;
	}	
	/**
	*	getPerformedDate ()
	*   @return returns the date of the work performed
	*/
	public Date getPerformedDate(){
		return performedDate;
	}	
	/**
	*	toString ()
	*   @return returns a string representation of the object  
	*/	
	public String toString(){
		return "["+performedDate+"] "+logEntry+"";
	}
}