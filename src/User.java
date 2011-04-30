import java.util.*;
import java.util.UUID;
/** The User class used to store user information and send user information between
*	a client and a server and vice versa 
*	@author Bruce Kennedy
*	@version 1.0
*/
public class User implements java.io.Serializable{
	
	private int id;
	private String name;
	private String logon;
	private String password;
	private UserRole role;

	/**
	*	User(int i, String l, String p, String n, UserRole r)
	*	@param i is the user's id (0 if not yet stored in database);
	*	@param l is the user's logon
	*	@param p is the password (this needs to be hashed)
	*	@param n is the user's name
	*	@param r is the user's role
	*/	
	public User(int i, String l, String p, String n, UserRole r){
		id = i;
		name = n;
		logon = l;
		password =p;
		role = r;
	}
	/**
	*	User(String l, String p, String n, UserRole r)
	*	@param l is the user's logon
	*	@param p is the password (this needs to be hashed)
	*	@param n is the user's name
	*	@param r is the user's role
	*/	
	public User(String l, String p, String n, UserRole r){
		id = 0;
		name = n;
		logon = l;
		password =p;
		role = r;
	}
	/**
	*	getID ()
	*   @return returns the user's UUID value
	*/	
	public int getID(){
		return id;
	}
	/**
	*	getName ()
	*   @return returns the user's name
	*/
	public String getName(){
		return name;
	}
	/**
	*	getLogon ()
	*   @return returns the user's logon
	*/
	public String getLogon(){
		return logon;
	}	
	/**
	*	getPassword ()
	*   @return returns the user's password
	*/
	public String getPassword(){
		return password;
	}
	/**
	*	getRole ()
	*   @return returns the user's role
	*/
	public UserRole getRole(){
		return role;
	}
	/**
	*	toString ()
	*   @return returns a string representation of the user with both name and id.  
	*		Displays in a JList.
	*/	
	public String toString(){
		return ""+name+" ["+logon+"]";
	}
}