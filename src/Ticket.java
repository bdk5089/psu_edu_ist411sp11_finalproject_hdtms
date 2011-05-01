/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/

import java.util.*;
import java.sql.Timestamp;

public class Ticket implements java.io.Serializable{
	
	private int id;
	private String desc;
	private String res;
	private ResolutionCode resCode;
	private StatusCode statusCode;
	private User checkedOutBy;
	private Timestamp checkedOutDate;
	private ArrayList<TicketLogEntry> logEntries = new ArrayList<TicketLogEntry>();
	
	/**
	*	Ticket(int i, String d, String r, ResolutionCode rc, StatusCode sc, User cb, Timestamp cbd)
	*	@param i is the Ticket id (0 if not stored/retrieved from database);
	*	@param d is the ticket description (i.e., what's wrong);
	*	@param r is the ticket resolution (i.e., final fix);
	*	@param rc is the resolution code pertaining to the resolution;
	*	@param sc is the status code representing current status;
	*	@param cb is the User the ticket is checked out by;
	*	@param cbd is the date/time the User checked out the ticket;
	*/	
	public Ticket(int i, String d, String r, ResolutionCode rc, StatusCode sc, User cb, Timestamp cbd){
		id = i;
		desc = d;
		res = r;
		resCode = rc;
		statusCode = sc;
		checkedOutBy = cb;
		checkedOutDate = cbd;
	}
	/**
	*	Ticket(String d, String r, ResolutionCode rc, StatusCode sc, User cb, Timestamp cbd)
	*	@param d is the ticket description (i.e., what's wrong);
	*	@param r is the ticket resolution (i.e., final fix);
	*	@param rc is the resolution code pertaining to the resolution;
	*	@param sc is the status code representing current status;
	*	@param cb is the User the ticket is checked out by;
	*	@param cbd is the date/time the User checked out the ticket;
	*/	
	public Ticket(String d, String r, ResolutionCode rc, StatusCode sc, User cb, Timestamp cbd){
		id = 0;
		desc = d;
		res = r;
		resCode = rc;
		statusCode = sc;
		checkedOutBy = cb;
		checkedOutDate = cbd;
	}
	
	/**
	*	getID ()
	*   @return returns the ticket ID value
	*/	
	public int getID(){
		return id;
	}
	
	/**
	*	getDesc ()
	*   @return returns the ticket summary description
	*/
	public String getDesc(){
		return desc;
	}
	/**
	*	setDesc (String d)
	*	@param d the new description
	*/
	public void setDesc(String d){
		desc = d;
	}
		
	/**
	*	getStatus ()
	*   @return returns the status code object
	*/
	public StatusCode getStatusCode(){
		return statusCode;
	}
	/**
	*	setStatusCode (StatusCode sc)
	*   @param sc is the new StatusCode object
	*/
	public void setStatusCode(StatusCode cs){
		statusCode = cs;
	}	
	
	
	/**
	*	getResolution ()
	*   @return returns the resolution
	*/
	public String getResolution(){
		return res;
	}
	/**
	*	setResolution ()
	*   @param r new resolution string
	*/
	public void setResolution(String r){
		res = r;
	}	
	
	/**
	*	getResolutionCode ()
	*   @return returns the resolutionCode
	*/
	public ResolutionCode getResolutionCode(){
		return resCode;
	}
	
	/**
	*	setResolutionCode(ResolutionCode cr)
	*   @param cr is the new StatusCode object
	*/
	public void setResolutionCode(ResolutionCode cr){
		resCode = cr;
	}
	
	/**
	*	getCheckedOutBy ()
	*   @return returns the user who performed work
	*/
	public User getCheckedOutBy(){
		return checkedOutBy;
	}	
	/**
	*	getCheckedOutDate ()
	*   @return returns the date of the work performed
	*/
	public Timestamp getCheckedOutDate(){
		return checkedOutDate;
	}	
	
	/**
	*	toString ()
	*   @return returns a string representation of the object  
	*/	
	public String toString(){
		return ""+desc+" ["+getStatusCode()+"]";
	}
	
	/**
	*	getLogEntries ()
	*   @return returns an ArrayList of associated TicketLogEntry objects.
	*/		
	public ArrayList<TicketLogEntry> getLogEntries(){
		return logEntries;
	}
	
	/**
	*	setLogEntries(ArrayList<TicketLogEntry> c)
	*   @param c an ArrayList of associated TicketLogEntry objects.
	*/		
	public void setLogEntries(ArrayList<TicketLogEntry> c){
		logEntries = c;
	}
	
	/**
	*	addLogEntry(TicketLogEntry l)
	*   @param l is a TicketLogEntry that will be added to the logEntries.
	*	
	*	@return returns true if successful, false if otherwise.
	*/			
	public boolean addLogEntry(TicketLogEntry l){
		try{
			logEntries.add(l);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
}