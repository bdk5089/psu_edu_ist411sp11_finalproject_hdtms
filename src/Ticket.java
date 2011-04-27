import java.util.*;

/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/
public class Ticket implements java.io.Serializable{
	
	private int id;
	private String desc;
	private String res;
	private ResolutionCode resCode;
	private StatusCode statusCode;
	private User checkedOutBy;
	private Date checkedOutDate;
	private ArrayList<TicketLogEntry> logEntries;
	/**
	*	Ticket(int i, String d, String r, ResolutionCode rc, StatusCode sc, User cb, Date cbd)
	*	@param i is the Ticket id (0 if not stored/retrieved from database);
	*	@param d is the ticket description (i.e., what's wrong);
	*	@param r is the ticket resolution (i.e., final fix);
	*	@param rc is the resolution code pertaining to the resolution;
	*	@param sc is the status code representing current status;
	*	@param cb is the User the ticket is checked out by;
	*	@param cbd is the date the User checked out the ticket;
	*/	
	public Ticket(int i, String d, String r, ResolutionCode rc, StatusCode sc, User cb, Date cbd){
		id = i;
		desc = d;
		res = r;
		resCode = rc;
		statusCode = sc;
		checkedOutBy = cb;
		checkedOutDate = cbd;
	}
	/**
	*	Ticket(String d, String r, ResolutionCode rc, StatusCode sc, User cb, Date cbd)
	*	@param d is the ticket description (i.e., what's wrong);
	*	@param r is the ticket resolution (i.e., final fix);
	*	@param rc is the resolution code pertaining to the resolution;
	*	@param sc is the status code representing current status;
	*	@param cb is the User the ticket is checked out by;
	*	@param cbd is the date the User checked out the ticket;
	*/	
	public Ticket(String d, String r, ResolutionCode rc, StatusCode sc, User cb, Date cbd){
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
	*	returns the ticket ID value
	*/	
	public int getID(){
		return id;
	}
	/**
	*	getDesc ()
	*	returns the ticket summary description
	*/
	public String getDesc(){
		return desc;
	}
	/**
	*	getStatus ()
	*	returns the status code object
	*/
	public StatusCode getStatusCode(){
		return statusCode;
	}
	/**
	*	getResolution ()
	*	returns the resolution
	*/
	public String getResolution(){
		return res;
	}
	/**
	*	getResolutionCode ()
	*	returns the resolutionCode
	*/
	public ResolutionCode getResolutionCode(){
		return resCode;
	}
	/**
	*	getCheckedOutBy ()
	*	returns the user who performed work
	*/
	public User getCheckedOutBy(){
		return checkedOutBy;
	}	
	/**
	*	getCheckedOutDate ()
	*	returns the date of the work performed
	*/
	public Date getCheckedOutDate(){
		return checkedOutDate;
	}	
	/**
	*	toString ()
	*	returns a string representation of the object  
	*/	
	public String toString(){
		return ""+desc+" ["+getStatusCode()+"]";
	}
	
	public ArrayList<TicketLogEntry> getLogEntries(){
		return logEntries;
	}
	public void setLogEntries(ArrayList<TicketLogEntry> c){
		logEntries = c;
	}
	
	public boolean addLogEntry(TicketLogEntry l){
		try{
			logEntries.add(l);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
}