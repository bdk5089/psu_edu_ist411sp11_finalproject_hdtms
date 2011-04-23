import java.util.*;

/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/
public class UserRole implements java.io.Serializable{
	
	private int id;
	private String name;
	private String desc;
	/**
	*	UserRole (String n, String i)
	*	@param i is role id
	*	@param n is the role name (i.e., Help Desk Administrator)
	*	@param d is the role description
	*/	
	public UserRole (int i, String n, String d){
		id = i;
		name = n;
		desc = d;
	}
	/**
	*	UserRole (String n, String i)
	*	@param i is role id
	*/	
	public UserRole(int i){
		id = i;
		name = "<name>";
		desc = "<description>";
	}

	/**
	*	getID ()
	*	returns the role ID
	*/
	public int getID(){
		return id;
	}
	/**
	*	getName ()
	*	returns the role name
	*/
	public String getName(){
		return name;
	}
	/**
	*	getDesc ()
	*	returns the role description
	*/	
	public String getDesc(){
		return desc;
	}
	/**
	*	toString ()
	*	returns a string representation of the user with both name and id.  
	*	Displays in a JList.
	*/	
	public String toString(){
		return ""+name+": "+desc+"]";
	}
}