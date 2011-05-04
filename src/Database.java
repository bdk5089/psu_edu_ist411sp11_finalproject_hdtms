/*
	Filename: Database.Java
	Classname: Database
	Comments: The Database access for our Ticketing system
*/

/** The Database class handles SQL and ODBC connection to the database
*	@author Eric So, Bruce Kennedy
*	@version 1.0
*/

import java.util.*;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.lang.Integer;
import java.lang.reflect.Array;

public class Database {

	/**
	*	main(String[] args)
	*	@param args is a String array of command-line values used to 
	*		instantiate the program....this method is used primarily for 
	*		database testing purposes.
	*/
	public static void main(String[] args){
		Database d = new Database(args[0]);
		
		System.out.println("**************************************");
		System.out.println("**** TESTING DATABASE............ ****");
		System.out.println("**************************************");
		System.out.println("**** GETTING USERS                ****");
		ArrayList<User> users = d.getUsers();
		for(int i=0;i<users.size();i++){
			System.out.println("User : "+users.get(i));
		}
		System.out.println("**** GETTING STATUS CODES         ****");
		ArrayList<StatusCode> stati = d.getStatusCodes();
		for(int i=0;i<stati.size();i++){
			System.out.println("Status : "+stati.get(i));
		}		
		System.out.println("**** GETTING RESOLUTION CODES     ****");
		ArrayList<ResolutionCode> resi = d.getResolutionCodes();
		for(int i=0;i<resi.size();i++){
			System.out.println("Resolution : "+resi.get(i));
		}	

		System.out.println("**** GETTING SINGLE USER          ****");
		User user = d.getUserByLogon("bdk5089");
		System.out.println("User : "+user);
		
		System.out.println("**** CREATE A TICKET              ****");
		Ticket t = new Ticket("New Test Ticket", "", null, null, user, new Timestamp(new Date().getTime()));
		TicketLogEntry te = new TicketLogEntry(t.getID(), "Work performed goes here", user, new Timestamp(new Date().getTime()));
		t.addLogEntry(te);
		boolean succ = d.updateTicket(t);
		System.out.println("**** CREATED TICKET [success = "+succ +"]");
		
		System.out.println("**** GETTING TICKETS              ****");
		HashMap<String, Ticket> tickets = d.getActiveTickets();
        Iterator hashIterator = tickets.keySet().iterator();
        while(hashIterator.hasNext()) {
			Object hashIndex = hashIterator.next();
			Ticket ticket = tickets.get(hashIndex);
				System.out.println("Ticket :" +ticket.toString());
			ArrayList<TicketLogEntry> logs = ticket.getLogEntries();
			for (int j=0;j<logs.size();j++){
				System.out.println("   Log : "+logs.get(j));
			}			
        }


		
	}
	
	private Connection connect;
	
	/**
	*	Database(String s)
	*	@param s is a reference to an ODBC connection in the local machine
	*		ODBC manager.
	*/	
	public Database(String s){
		try{
			String url = "jdbc:odbc:"+s;
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			System.out.println("   Database Connection: "+url);
			connect = DriverManager.getConnection(url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	*	Database(Connection c)
	*	@param c is a Connection object to a database
	*/		
	public Database(Connection c){
		connect = c;
	}
	/**
	*	checkInTicket(Ticket t)
	*	@param t is the ticket that is being checked in  
	*	
	*	@return returns true if successful, false if otherwise
	*/	
	public boolean checkInTicket(Ticket t){
		boolean success = true;
		try{
			Statement update = connect.createStatement();
			String sql = "UPDATE tblTickets SET "
					+ "TicketCheckedOutByUserID = NULL "
					+ ",TicketCheckedOutDateTime = NULL "
					+ " WHERE TicketID = ?" ;
			PreparedStatement sqlment = connect.prepareStatement(sql);
			sqlment.setInt(1,t.getID());
			sqlment.executeUpdate(); 
			update.close();			
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;		
	}
	/**
	*	checkOutTicket(int id, User u)
	*	@param id is the ID value of the ticket being checked out.  
	*	@param u is the User that is checking out the ticket.  
	*	
	*	@return returns true if successful, false if otherwise
	*/	
	public boolean checkOutTicket(int id, User u){
		boolean success = true;
		try{
			Statement update = connect.createStatement();
			String sql = "UPDATE tblTickets SET "
					+ " TicketCheckedOutByUserID = ? "
					+ ",TicketCheckedOutDateTime = ? "
					+ " WHERE TicketID = ?" ;
			PreparedStatement sqlment = connect.prepareStatement(sql);
			sqlment.setInt(1,u.getID());
			sqlment.setTimestamp(2,new Timestamp(new Date().getTime()));
			sqlment.setInt(3,id);
			sqlment.executeUpdate(); 
			update.close();
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;	
	}
	/**
	*	updateTicket(Ticket t)
	*	@param t is the ticket that is to be updated.  
	*       If the ticket ID value is 0, then a new record is inserted into the database.
	*	   	If the ticket ID value is > 0 then the record is updated.  
	*	
	*	@return returns true if successful, false if otherwise
	*/		
	public boolean updateTicket(Ticket t){
		boolean success = true;
		int newID = 0;
		try{
			//Don't insert the ticket if it already has an ID,
			//having an ID means that it already is in the database.
			if (t.getID() == 0){
				synchronized(this){
					//This section is synchronized so that the proper newly created ticket ID can be gotten
					//and then used to updated the TicketLogEntry objects that need to be updated in the 
					//database.
					Statement insert = connect.createStatement();
					String sql = "INSERT INTO tblTickets (TicketSummaryDesc, TicketStatusCodeID, TicketResolutionDesc, TicketResolutionCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime) VALUES( ?,  ?,  ?,  ?,  ?,  ? )";
					PreparedStatement sqlment = connect.prepareStatement(sql);
					sqlment.setString(1,t.getDesc());
					if (t.getStatusCode() != null){
					sqlment.setInt(2,t.getStatusCode().getID());
					}else{
					sqlment.setInt(2,1);
					}
					sqlment.setString(3,t.getResolution());
					if (t.getResolutionCode() != null){
					sqlment.setInt(4,t.getResolutionCode().getID());
					}else{
					sqlment.setInt(4,1);
					}
					sqlment.setInt(5,t.getCheckedOutBy().getID());
					sqlment.setTimestamp(6,t.getCheckedOutDate());
					sqlment.executeUpdate(); 	
					insert.close();
					//Get newly created TicketID value
					Statement select = connect.createStatement();
					String sql2 = "SELECT MAX(TicketID) AS newID FROM tblTickets";
					ResultSet results = select.executeQuery(sql2);
					while (results.next()){
						newID = results.getInt(1);
					}
					System.out.println("ADDED TICKET ID: "+newID);
					select.close();
				}	
				ArrayList<TicketLogEntry> logs = t.getLogEntries();
				if (logs != null){
					for(int i=0;i<logs.size();i++){
						//Update the ticketID reference in the TicketLogEntry object
						logs.get(i).setTicketID(newID);
						//Send the TicketLogEntry object to be updated in the database
						updateTicketLogEntry(logs.get(i));
					}
				}				
			//We could do an update of the ticket based on the fact that 
			//there already is an ID
			}else if (t.getID() >0){
				Statement update = connect.createStatement();
				String sql = "UPDATE tblTickets SET "
						+ " TicketSummaryDesc = ? "
						+ ",TicketStatusCodeID = ? "
						+ ",TicketResolutionDesc = ? "
						+ ",TicketResolutionCodeID = ? "
						+ " WHERE TicketID = ? " ;
				PreparedStatement sqlment = connect.prepareStatement(sql);
				sqlment.setString(1,t.getDesc());
				sqlment.setInt(2,t.getStatusCode().getID());
				sqlment.setString(3,t.getResolution());
				sqlment.setInt(4,t.getResolutionCode().getID());
				sqlment.setInt(5,t.getID());
				sqlment.executeUpdate(); 
				update.close();
				
				ArrayList<TicketLogEntry> logs = t.getLogEntries();
				if (logs != null){
					for(int i=0;i<logs.size();i++){
						//Update the ticketID reference in the TicketLogEntry object
						// {This may not be necessary}
						logs.get(i).setTicketID(t.getID());
						//Send the TicketLogEntry object to be updated in the database
						updateTicketLogEntry(logs.get(i));
					}
				}	
			}

		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}catch(Exception e){
			success = false;
		}
		return success;
	}
	
	/**
	*	getTickets()
	*	Used to get all the tickets from the tblTickets table  
	*	
	*	@return returns a HashMap of the Ticket objects associated with 
	*		the records retrieved from the tblTickets table
	*/	
	public HashMap<String, Ticket> getTickets() {
		HashMap<String, Ticket> recordSet = new HashMap<String, Ticket>();
		Ticket record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketID, TicketSummaryDesc, TicketStatusCodeID, TicketResolutionDesc, TicketResolutionCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime FROM tblTickets";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketID = results.getInt(1);
				String TicketSummaryDesc= results.getString(2);
				StatusCode TicketStatusCodeID = getStatusCodeByID(results.getInt(3));
				String TicketResolutionDesc = results.getString(4);
				ResolutionCode TicketResolutionCodeID = getResolutionCodeByID(results.getInt(5));
				User TicketCheckedOutByUserID = getUserByID(results.getInt(6));
				Timestamp TicketCheckedOutDateTime = results.getTimestamp(7);
				
				// Convert the int TicketID to a String
				// Note: not sure if the nested contstructor call will work
				String ticketIDString = (new Integer(TicketID)).toString();
				
				record = new Ticket(TicketID, TicketSummaryDesc, TicketResolutionDesc, TicketResolutionCodeID, TicketStatusCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime);
				record.setLogEntries(getTicketLogEntriesByTicket(TicketID));
				
				recordSet.put(ticketIDString, record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	/**
	*	getActiveTickets()
	*	Used to get all the tickets that have StatusID values of 1,2, or 10. 
	*	
	*	@return returns a HashMap of the Ticket objects associated with the records retrieved from the tblTickets table
	*/
	public HashMap<String, Ticket> getActiveTickets(){
		ArrayList<Integer> s = new ArrayList<Integer>(3);
		s.add(1);
		s.add(2);
		s.add(10);
		return getTicketsByStatus(s);		
	}
	
	/**
	*	getTicketsByStatus(ArrayList<Integer> sl)
	*	@param sl is the ArrayList of status ids that correspond with thos tickets you want 
	* 		to receive.
	*	
	*	@return returns a HashMap of the Ticket objects associated with the records retrieved from the tblTickets table
	*/	
	public HashMap<String, Ticket> getTicketsByStatus(ArrayList<Integer> sl){
		String list = sl.toString();
		list = list.replace("[","(");
		list = list.replace("]",")");
		HashMap<String, Ticket> recordSet = new HashMap<String, Ticket>();
		Ticket record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketID, TicketSummaryDesc, TicketStatusCodeID, TicketResolutionDesc, TicketResolutionCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime FROM tblTickets WHERE TicketStatusCodeID IN "+list+"";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketID = results.getInt(1);
				String TicketSummaryDesc= results.getString(2);
				StatusCode TicketStatusCodeID = getStatusCodeByID(results.getInt(3));
				String TicketResolutionDesc = results.getString(4);
				ResolutionCode TicketResolutionCodeID = getResolutionCodeByID(results.getInt(5));
				User TicketCheckedOutByUserID = getUserByID(results.getInt(6));
				Timestamp TicketCheckedOutDateTime = results.getTimestamp(7);
				
				// Convert the int TicketID to a String
				// Note: not sure if the nested contstructor call will work
				String ticketIDString = (new Integer(TicketID)).toString();
				
				record = new Ticket(TicketID, TicketSummaryDesc, TicketResolutionDesc, TicketResolutionCodeID, TicketStatusCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime);
				record.setLogEntries(getTicketLogEntriesByTicket(TicketID));
				
				recordSet.put(ticketIDString, record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	/**
	*	getTicketByID(int id)
	*	@param id the ID of the ticket that you want to receive.
	*	
	*	@return returns the Ticket object associated with the record in the tblTickets table
	*/	
	public Ticket getTicketByID(int id){
		Ticket record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketID, TicketSummaryDesc, TicketStatusCodeID, TicketResolutionDesc, TicketResolutionCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime FROM tblTickets WHERE TicketID = "+id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketID = results.getInt(1);
				String TicketSummaryDesc= results.getString(2);
				StatusCode TicketStatusCodeID = getStatusCodeByID(results.getInt(3));
				String TicketResolutionDesc = results.getString(4);
				ResolutionCode TicketResolutionCodeID = getResolutionCodeByID(results.getInt(5));
				User TicketCheckedOutByUserID = getUserByID(results.getInt(6));
				Timestamp TicketCheckedOutDateTime = results.getTimestamp(7);
				record = new Ticket(TicketID, 
									TicketSummaryDesc, 
									TicketResolutionDesc, 
									TicketResolutionCodeID, 
									TicketStatusCodeID, 
									TicketCheckedOutByUserID, 
									TicketCheckedOutDateTime);
				record.setLogEntries(getTicketLogEntriesByTicket(TicketID));
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	*	updateTicketLogEntry(TicketLogEntry t)
	*	@param t is the TicketLogEntry that is to be stored on the database.
	*		If the id of the TicketLogEntry is 0 then a new record is inserted.
	*		If the id is >0 then the record is updated. 
	*		THIS ASSUMES THAT NO tickets in 
	*		client memory are missing from the database.
	*	
	*	@return returns true if successfull, false if otherwise
	*/			
	public boolean updateTicketLogEntry(TicketLogEntry t){
		boolean success = true;
		try{
			//Only insert ticket log entry if there is no ID, 
			//means it hasn't been submitted to db
			if (t.getID() == 0){
				Statement insert = connect.createStatement();
				String sql = "INSERT INTO tblTicketWorkLogs (TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime) VALUES(?, ?, ?, ?)";
				PreparedStatement sqlment = connect.prepareStatement(sql);
				sqlment.setInt(1,t.getTicketID());
				sqlment.setString(2,t.getEntry());
				sqlment.setInt(3,t.getPerformedBy().getID());
				sqlment.setTimestamp(4,t.getPerformedDate());
				sqlment.executeUpdate(); 
				insert.close();
			//OR if there is an id value then update the ticket log entry
			}else if (t.getID() >0){
				Statement update = connect.createStatement();
				String sql = "UPDATE tblTicketWorkLogs SET "
						+ " TicketID = ? "
						+ ",TicketWorkLogEntry = ? "
						+ ",TicketWorkPerformedByUserID = ? "
						+ ",TicketWorkLogEntryDateTime = ? "
						+ " WHERE TicketWorkLogID = ? " ;
				PreparedStatement sqlment = connect.prepareStatement(sql);
				sqlment.setInt(1,t.getTicketID());
				sqlment.setString(2,t.getEntry());
				sqlment.setInt(3,t.getPerformedBy().getID());
				sqlment.setTimestamp(4,t.getPerformedDate());
				sqlment.setInt(5,t.getID());
				sqlment.executeUpdate(); 
				update.close();
			}
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	*	getTicketLogEntriesByTicket(Ticket t)
	*	@param t is the Ticket object reference of the log entries receieved
	*	
	*	@return returns an ArrayList of TicketLogEntry objects associated with 
	*		the records in the tblTicketWorkLogs table in the database
	*/	
	public ArrayList<TicketLogEntry> getTicketLogEntriesByTicket(Ticket t){
		return getTicketLogEntriesByTicket(t.getID());
	}
	/**
	*	getTicketLogEntriesByTicket(int i)
	*	@param i is the ID of the ticket to which the log entries are referenced in the database.
	*	
	*	@return returns the TicketLogEntry object associated with the record in the tblTicketWorkLogs in the database
	*/		
	public ArrayList<TicketLogEntry> getTicketLogEntriesByTicket(int i){
		ArrayList<TicketLogEntry> recordSet = new ArrayList<TicketLogEntry>();
		TicketLogEntry record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketWorkLogID, TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime FROM tblTicketWorkLogs WHERE TicketID = "+i;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketWorkLogID = results.getInt(1);
				int TicketID = results.getInt(2);
				String TicketWorkLogEntry = results.getString(3);
				User TicketWorkPerformedByUserID = getUserByID(results.getInt(4));
				Timestamp TicketWorkLogEntryDateTime = results.getTimestamp(5);
				record = new TicketLogEntry(TicketWorkLogID, 
											TicketID,
											TicketWorkLogEntry, 
											TicketWorkPerformedByUserID, 
											TicketWorkLogEntryDateTime);
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	/**
	*	getTicketLogEntryByID(int id)
	*	@param i is the ID of the ticket log entry as stored in the database.
	*	
	*	@return returns the TicketLogEntry object associated with the record in the tblTicketWorkLogs in the database
	*/		
	public TicketLogEntry getTicketLogEntryByID(int id){
		TicketLogEntry record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketWorkLogID, TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime FROM  tblTicketWorkLogs WHERE TicketWorkLogID = " + id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketWorkLogID = results.getInt(1);
				int TicketID = results.getInt(2);
				String TicketWorkLogEntry = results.getString(3);
				User TicketWorkPerformedByUserID = getUserByID(results.getInt(4));
				Timestamp TicketWorkLogEntryDateTime = results.getTimestamp(5);
				record = new TicketLogEntry(TicketWorkLogID, 
											TicketID,
											TicketWorkLogEntry, 
											TicketWorkPerformedByUserID, 
											TicketWorkLogEntryDateTime);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	*	getUsers()
	*	Returns the list of users as recorded in the database.
	*
	*	@return returns an ArrayList of User objects associated with the records from the tblUsers
	*/			
	public ArrayList<User> getUsers(){
		ArrayList<User> recordSet = new ArrayList<User>();
		User record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT UserID, UserLogon, UserName, UserRoleID FROM  tblUsers";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int UserID = results.getInt(1);
				String UserLogon= results.getString(2);
				String UserName = results.getString(3);
				UserRole UserRoleID = getUserRoleByID(results.getInt(4));
				record = new User(UserID, UserLogon, "<hidden>", UserName, UserRoleID);
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	/**
	*	getUserByID(int id)
	*	@param id is the id of the User as recorded in the database. 
	*
	*	@return returns the User object associated with the record from the tblUsers
	*/		
	public User getUserByID(int id){
		User record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT UserID, UserLogon, UserName, UserRoleID FROM  tblUsers WHERE UserID = " + id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int UserID = results.getInt(1);
				String UserLogon= results.getString(2);
				String UserName = results.getString(3);
				UserRole UserRoleID = getUserRoleByID(results.getInt(4));
				record = new User(UserID, UserLogon, "<hidden>", UserName, UserRoleID);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	*	getUserByID(int id)
	*	@param id is the id of the User as recorded in the database. 
	*
	*	@return returns the User object associated with the record from the tblUsers
	*/		
	public User getUserByLogon(String l){
		User record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT UserID, UserLogon, UserName, UserRoleID FROM  tblUsers WHERE UserLogon = '" + l + "'";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int UserID = results.getInt(1);
				String UserLogon= results.getString(2);
				String UserName = results.getString(3);
				UserRole UserRoleID = getUserRoleByID(results.getInt(4));
				record = new User(UserID, UserLogon, "<hidden>", UserName, UserRoleID);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	*	getUserRoles()
	*
	*	@return returns an ArrayList of the UserRole objects associated with the records from the tblUserRoles
	*/			
	public ArrayList<UserRole> getUserRoles(){
		ArrayList<UserRole> recordSet = new ArrayList<UserRole>();
		UserRole record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT * FROM  tblUserRoles";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int UserRoleID = results.getInt(1);
				String UserRoleName= results.getString(2);
				String UserRoleDescription = results.getString(3);
				record = new UserRole(UserRoleID, UserRoleName, UserRoleDescription);
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	/**
	*	getUserRoleByID(int id)
	*	@param id is the id of the UserRole as recorded in the database. 
	*
	*	@return returns the UserRole object associated with the record from the tblUserRoles
	*/		
	public UserRole getUserRoleByID(int id){
		UserRole record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT * FROM  tblUserRoles WHERE UserRoleID = " + id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int UserRoleID = results.getInt(1);
				String UserRoleName= results.getString(2);
				String UserRoleDescription = results.getString(3);
				record = new UserRole(UserRoleID, UserRoleName, UserRoleDescription);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	*	getResolutionCodes()
	*
	*	@return returns an ArrayList of the ResolutionCode objects associated with the records from the tblResolutionCodes
	*/		
	public ArrayList<ResolutionCode> getResolutionCodes(){
		ArrayList<ResolutionCode> recordSet = new ArrayList<ResolutionCode>();
		ResolutionCode record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT * FROM  tblResolutionCodes";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int ResolutionCodeID = results.getInt(1);
				String ResolutionName= results.getString(2);
				String ResolutionDescription = results.getString(3);
				record = new ResolutionCode(ResolutionCodeID, ResolutionName, ResolutionDescription);
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	/**
	*	getResolutionCodeByID(int id)
	*	@param id is the id of the ResolutionCode as recorded in the database. 
	*
	*	@return returns the ResolutionCode object associated with the record from the tblResolutionCodes
	*/
	public ResolutionCode getResolutionCodeByID(int id){
		ResolutionCode record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT * FROM  tblResolutionCodes WHERE ResolutionCodeID = " + id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int ResolutionCodeID = results.getInt(1);
				String ResolutionName= results.getString(2);
				String ResolutionDescription = results.getString(3);
				record = new ResolutionCode(ResolutionCodeID, ResolutionName, ResolutionDescription);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	/**
	*	getStatusCodes()
	*
	*	@return returns an ArrayList of the StatusCode objects associated with the records from the tblStatusCodes
	*/			
	public ArrayList<StatusCode> getStatusCodes(){
		ArrayList<StatusCode> recordSet = new ArrayList<StatusCode>();
		StatusCode record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT * FROM  tblStatusCodes";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int StatusCodeID = results.getInt(1);
				String StatusName= results.getString(2);
				String StatusDescription = results.getString(3);
				record = new StatusCode(StatusCodeID, StatusName, StatusDescription);
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}	
	
	/**
	*	getStatusCodeByID(int id)
	*	@param id is the id of the StatusCode as recorded in the database. 
	*
	*	@return returns the StatusCode object associated with the record from the tblStatusCodes
	*/
	public StatusCode getStatusCodeByID(int id){
		StatusCode record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT * FROM  tblStatusCodes WHERE StatusCodeID = " + id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int StatusCodeID = results.getInt(1);
				String StatusName= results.getString(2);
				String StatusDescription = results.getString(3);
				record = new StatusCode(StatusCodeID, StatusName, StatusDescription);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}	

}