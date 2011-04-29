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
import java.sql.*;
import java.sql.Date;
import java.lang.Integer;
import java.lang.reflect.Array;


public class Database {
		
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

		
		System.out.println("**** GETTING TICKETS              ****");
		ArrayList<Integer> s = new ArrayList(2);
		s.add(1);
		s.add(2);
		ArrayList<Ticket> tickets = d.getTicketsByStatus(s);
		for(int i=0;i<tickets.size();i++){
			System.out.println("Ticket : "+tickets.get(i));
			ArrayList<TicketLogEntry> logs = tickets.get(i).getLogEntries();
			for (int j=0;j<logs.size();j++){
				System.out.println("   Log : "+logs.get(j));
			}
		}
		System.out.println("**** GETTING SINGLE USER          ****");
		User user = d.getUserByLogon("bdk5089");
		System.out.println("User : "+user);
		
	}
	
	private Connection connect;
	
	public Database(String s){
		try{
			String url = "jdbc:odbc:"+s;
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			connect = DriverManager.getConnection(url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Database(Connection c){
		connect = c;
	}
	
	public boolean insertTicket(Ticket t){
		boolean success = true;
		try{
			//Don't insert the ticket if it already has an ID,
			//having an ID means that it already is in the database.
			if (t.getID() == 0){
				Statement insert = connect.createStatement();
				String sql = "INSERT INTO tblTickets (TicketSummaryDesc, TicketStatusCodeID, TicketResolutionDesc, TicketResolutionCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime) VALUES("
						+ "'" + t.getDesc() + "', "
						+ "'" + t.getStatusCode().getID() + "', "
						+ "'" + t.getResolution() + "', "
						+ "'" + t.getResolutionCode().getID() + "', "
						+ "'" + t.getCheckedOutBy().getID() + "', "
						+ "#" + t.getCheckedOutDate() + "#"
						+ ")";
				insert.executeUpdate(sql);
				insert.close();
				
			//We could do an update of the ticket based on the fact that 
			//there already is an ID
			}else if (t.getID() >0){
				Statement update = connect.createStatement();
				String sql = "UPDATE tblTickets SET "
						+ " TicketSummaryDesc = '"+t.getDesc()+"'"
						+ ",TicketStatusCodeID = "+t.getStatusCode().getID()+""
						+ ",TicketResolutionDesc = '"+t.getResolution()+"'"
						+ ",TicketResolutionCodeID = "+t.getResolutionCode().getID()+""
						+ ",TicketCheckedOutByUserID = "+t.getCheckedOutBy().getID()+""
						+ ",TicketCheckedOutDateTime = #"+t.getCheckedOutDate()+"#"
						+ " WHERE TicketID = "+t.getID()+"" ;
				update.executeUpdate(sql);
				update.close();
			}
			ArrayList<TicketLogEntry> logs = t.getLogEntries();
			for(int i=0;i<logs.size();i++){
				insertTicketLogEntry(logs.get(i));
			}
			
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	// getTicketsWithID returns a HashMap of the Ticket objects mapped to their IDs as Strings
	public HashMap<String, Ticket> getTicketsWithID() {
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
				Date TicketCheckedOutDateTime = results.getDate(7);
				
				// Convert the int TicketID to a String
				// Note: not sure if the nested contstructor call will work
				String ticketIDString = (new Integer(TicketID)).toString();
				
				record = new Ticket(TicketID, TicketSummaryDesc, TicketResolutionDesc, TicketResolutionCodeID, TicketStatusCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime);
				recordSet.put(ticketIDString, record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	public ArrayList<Ticket> getTickets(){
		ArrayList<Ticket> recordSet = new ArrayList<Ticket>();
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
				Date TicketCheckedOutDateTime = results.getDate(7);
				record = new Ticket(TicketID, 
									TicketSummaryDesc, 
									TicketResolutionDesc, 
									TicketResolutionCodeID, 
									TicketStatusCodeID, 
									TicketCheckedOutByUserID, 
									TicketCheckedOutDateTime);
				record.setLogEntries(getTicketLogEntriesByTicket(record.getID()));
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	
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
				Date TicketCheckedOutDateTime = results.getDate(7);
				record = new Ticket(TicketID, 
									TicketSummaryDesc, 
									TicketResolutionDesc, 
									TicketResolutionCodeID, 
									TicketStatusCodeID, 
									TicketCheckedOutByUserID, 
									TicketCheckedOutDateTime);
				record.setLogEntries(getTicketLogEntriesByTicket(record.getID()));
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
	public ArrayList<Ticket> getTicketsByStatus(ArrayList<Integer> sc){
		String list = sc.toString();
		list = list.replace("[","(");
		list = list.replace("]",")");
		ArrayList<Ticket> recordSet = new ArrayList<Ticket>();
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
				Date TicketCheckedOutDateTime = results.getDate(7);
				record = new Ticket(TicketID, 
									TicketSummaryDesc, 
									TicketResolutionDesc, 
									TicketResolutionCodeID, 
									TicketStatusCodeID, 
									TicketCheckedOutByUserID, 
									TicketCheckedOutDateTime);
				record.setLogEntries(getTicketLogEntriesByTicket(record.getID()));
				recordSet.add(record);
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return recordSet;
	}
	
	public boolean insertTicketLogEntry(TicketLogEntry t){
		boolean success = true;
		try{
			//Only insert ticket log entry if there is no ID, 
			//means it hasn't been submitted to db
			if (t.getID() == 0){
				Statement insert = connect.createStatement();
				String sql = "INSERT INTO tblTicketWorkLogs (TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime) VALUES("
						+ "'" + t.getTicketID() + "', "
						+ "'" + t.getEntry() + "', "
						+ "'" + t.getPerformedBy().getID() + "', "
						+ "#" + t.getPerformedDate() + "#"
						+ ")";
				insert.executeUpdate(sql);
				insert.close();
			//OR if there is an id value then update the ticket log entry
			}else if (t.getID() >0){
				Statement update = connect.createStatement();
				String sql = "UPDATE tblTicketWorkLogs SET "
						+ " TicketID = "+t.getTicketID()+""
						+ ",TicketWorkLogEntry = '"+t.getEntry()+"'"
						+ ",TicketWorkPerformedByUserID = "+t.getPerformedBy().getID()+""
						+ ",TicketWorkLogEntryDateTime = #"+t.getPerformedDate()+"#"
						+ " WHERE TicketWorkLogID = "+ t.getID();
				update.executeUpdate(sql);
				update.close();
			}
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	public ArrayList<TicketLogEntry> getTicketLogEntriesByTicket(Ticket t){
		return getTicketLogEntriesByTicket(t.getID());
	}
	
	public ArrayList<TicketLogEntry> getTicketLogEntriesByTicket(int i){
		ArrayList<TicketLogEntry> recordSet = new ArrayList<TicketLogEntry>();
		TicketLogEntry record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketWorkLogID, TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime FROM tblTicketWorkLog WHERE TicketID = "+i;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketWorkLogID = results.getInt(1);
				int TicketID = results.getInt(2);
				String TicketWorkLogEntry = results.getString(3);
				User TicketWorkPerformedByUserID = getUserByID(results.getInt(4));
				Date TicketWorkLogEntryDateTime = results.getDate(5);
				record = new TicketLogEntry(TicketWorkLogID, 
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
	public TicketLogEntry getTicketLogEntryByID(int id){
		TicketLogEntry record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketWorkLogID, TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime FROM  tblTicketWorkLog WHERE TicketWorkLogID = " + id;
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketWorkLogID = results.getInt(1);
				int TicketID = results.getInt(2);
				String TicketWorkLogEntry = results.getString(3);
				User TicketWorkPerformedByUserID = getUserByID(results.getInt(4));
				Date TicketWorkLogEntryDateTime = results.getDate(5);
				record = new TicketLogEntry(TicketWorkLogID, 
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