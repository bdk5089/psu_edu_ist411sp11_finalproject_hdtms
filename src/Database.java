import java.util.*;
import java.sql.*;
import java.sql.Date;


public class Database {
	
	public Connection connect;
	
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
			Statement insert = connect.createStatement();
			String sql = "INSERT INTO tblTickets (TicketID, TicketSummaryDesc, TicketStatusCodeID, TicketResolutionDesc, TicketResolutionCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime) VALUES("
					+ t.getID() + ", "
					+ "'" + t.getDesc() + "', "
					+ "'" + t.getStatusCode().getID() + "', "
					+ "'" + t.getResolution() + "', "
					+ "'" + t.getResolutionCode().getID() + "', "
					+ "'" + t.getCheckedOutBy().getID() + "', "
					+ "#" + t.getCheckedOutDate() + "#"
					+ "')";
			insert.executeUpdate(sql);
			insert.close();
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;
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
				record = new Ticket(TicketID, TicketSummaryDesc, TicketResolutionDesc, TicketResolutionCodeID, TicketStatusCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime);
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
				record = new Ticket(TicketID, TicketSummaryDesc, TicketResolutionDesc, TicketResolutionCodeID, TicketStatusCodeID, TicketCheckedOutByUserID, TicketCheckedOutDateTime);
				break;
			}
			results.close();
			select.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return record;
	}
	
/*  THIS NEEDS WORK */
	public boolean insertTicketLogEntry(TicketLogEntry t){
		boolean success = true;
		try{
			Statement insert = connect.createStatement();
			String sql = "INSERT INTO tblTickets (TicketWorkLogID, TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime) VALUES("
					+ t.getID() + ", "
					+ "'" + t.getDesc() + "', "
					+ "'" + t.getStatusCode().getID() + "', "
					+ "'" + t.getResolution() + "', "
					+ "'" + t.getResolutionCode().getID() + "', "
					+ "'" + t.getCheckedOutBy().getID() + "', "
					+ "#" + t.getCheckedOutDate() + "#"
					+ "')";
			insert.executeUpdate(sql);
			insert.close();
		}catch(SQLException e){
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	public ArrayList<TicketLogEntry> getTicketLogEntries(){
		ArrayList<TicketLogEntry> recordSet = new ArrayList<TicketLogEntry>();
		TicketLogEntry record = null;
		try{
			Statement select = connect.createStatement();
			String sql = "SELECT TicketWorkLogID, TicketID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime FROM  tblTicketWorkLog";
			ResultSet results = select.executeQuery(sql);
			while (results.next()){
				int TicketWorkLogID = results.getInt(1);
				int TicketID = results.getInt(2);
				String TicketWorkLogEntry = results.getString(3);
				User TicketWorkPerformedByUserID = getUserByID(results.getInt(4));
				Date TicketWorkLogEntryDateTime = results.getDate(5);
				record = new TicketLogEntry(TicketWorkLogID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime);
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
				record = new TicketLogEntry(TicketWorkLogID, TicketWorkLogEntry, TicketWorkPerformedByUserID, TicketWorkLogEntryDateTime);
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

	/*
	public void clear(){
		System.out.println("Clear all CDs");
		try{
			Statement clearCDs = connect.createStatement();
			String sql = "DELETE FROM CDs;";
			//System.out.println("Executing statement: " + sql);
			clearCDs.executeUpdate(sql);
			clearCDs.close();
			//System.out.println("CDs cleared successfully!");
		}catch(SQLException e){e.printStackTrace();}
	}
	
	public void insertCD(CD cd){
		System.out.println("Adding CD: " + cd.toString());
		try{
			Statement addCD = connect.createStatement();
			String sql = "INSERT INTO CDs (CD_id,title,artist) VALUES("
					+ cd.getNumber() + ", "
					+ "'" + cd.getTitle() + "', "
					+ "'" + cd.getArtist() + "')";

			//System.out.println("Executing statement: " + sql);
			addCD.executeUpdate(sql);
			addCD.close();
			//System.out.println("CD added successfully!");
		}catch(SQLException e){e.printStackTrace();}
	}
	
	public void removeCD(CD cd){
		System.out.println("Removing CD: " + cd.toString());
		try{
			Statement deleteCD = connect.createStatement();
			String sql = "DELETE FROM CDs WHERE CD_id = "+cd.getNumber()+";";

			//System.out.println("Executing statement: " + sql);
			deleteCD.executeUpdate(sql);
			deleteCD.close();
			//System.out.println("CD removed successfully!");
		}catch(SQLException e){e.printStackTrace();}
	}
	public ArrayList<CD> findAll(){
		ArrayList<CD> cds = new ArrayList<CD>();
		try{
			Statement selectAll = connect.createStatement();
			String sql = "SELECT * FROM CDs ";
			ResultSet results = selectAll.executeQuery(sql);
			while (results.next()){
				int number = results.getInt(1);
				String title= results.getString(2);
				String artist = results.getString(3);
				CD cd = new CD(number, title,artist);
				cds.add(cd);
			}
			results.close();
			selectAll.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return cds;
	}
	
	public ArrayList<CD> findByTitle(String s){
		System.out.println("Searching for title: '" + s+"'");
		ArrayList<CD> cds = new ArrayList<CD>();
		try{
			Statement selectByTitle = connect.createStatement();
			String sql = "SELECT * FROM CDs WHERE title LIKE '%" +s+ "%' " 
						+ "OR title LIKE '" +s+ "%' "
						+ "OR title LIKE '%" +s+ "' ";
			ResultSet results = selectByTitle.executeQuery(sql);
			while (results.next()){
				int number = results.getInt(1);
				String title= results.getString(2);
				String artist = results.getString(3);
				CD cd = new CD(number, title,artist);
				System.out.println("CD found: "+cd.toString());
				cds.add(cd);
			}
			results.close();
			selectByTitle.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return cds;
	}
	
	public ArrayList<CD> findByArtist(String s){
		System.out.println("Searching for artist: '" + s+"'");
		ArrayList<CD> cds = new ArrayList<CD>();
		try{
			Statement selectByArtist = connect.createStatement();
			String sql = "SELECT * FROM CDs WHERE artist LIKE '%" +s+ "%' " 
						+ "OR artist LIKE '" +s+ "%' "
						+ "OR artist LIKE '%" +s+ "' ";
			ResultSet results = selectByArtist.executeQuery(sql);
			while (results.next()){
				int number = results.getInt(1);
				String title= results.getString(2);
				String artist = results.getString(3);
				CD cd = new CD(number, title,artist);
				System.out.println("CD found: "+cd.toString());
				cds.add(cd);
			}
			results.close();
			selectByArtist.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return cds;
	}	
	*/
}