import java.util.*;

/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/
public class TicketLogEntry implements java.io.Serializable{
	
	private int id;
	private Ticket ticket;
	private String logEntry;
	private User performedBy;
	private Date performedDate;
	/**
	*	StatusCode (int i, String n, String d)
	*	@param i is the ID for data reference
	*	@param n is the name (displayed in UI)
	*	@param d is the description
	*/	
	public TicketLogEntry(int i, String l, User u, Date d){
		id = i;
		logEntry = l;
		performedBy = u;
		performedDate = d;
	}
	/**
	*	getID ()
	*	returns the user's UUID value
	*/	
	public int getID(){
		return id;
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
	*	getPerformedByDate ()
	*	returns the date of the work performed
	*/
	public Date getPerformedByDate(){
		return performedDate;
	}	
	/**
	*	toString ()
	*	returns a string representation of the object  
	*/	
	public String toString(){
		return ""+logEntry+"";
	}
}