import java.util.*;

/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/
public class StatusCode implements java.io.Serializable{
	
	private int id;
	private String name;
	private String desc;
	/**
	*	StatusCode (int i, String n, String d)
	*	@param i is the ID for data reference
	*	@param n is the name (displayed in UI)
	*	@param d is the description
	*/	
	public StatusCode(int i, String n, String d){
		id = i;
		name = n;
		desc = d;
	}
	/**
	*	getID ()
	*	returns the user's UUID value
	*/	
	public int getID(){
		return id;
	}
	/**
	*	getName ()
	*	returns the name
	*/
	public String getName(){
		return name;
	}
	/**
	*	getLogon ()
	*	returns the description
	*/
	public String getDesc(){
		return desc;
	}	
	/**
	*	toString ()
	*	returns a string representation of the object  
	*/	
	public String toString(){
		return ""+name+"";
	}
}